package com.onlineauctions.onlineauctions.annotation;

import com.onlineauctions.onlineauctions.pojo.respond.Result;
import com.onlineauctions.onlineauctions.pojo.type.ResultCode;
import com.onlineauctions.onlineauctions.pojo.type.Role;
import com.onlineauctions.onlineauctions.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
@Order(1)
public class PermissionAspect {
    //先用AOP实现，其他有时间再看看能不能直接用原生实现


    /**
     * 方法注解切面
     */
    @Pointcut("@annotation(com.onlineauctions.onlineauctions.annotation.Permission)")
    private void annotationPointCut() {
    }

    /**
     * 类注解切面
     */
    @Pointcut("@within(com.onlineauctions.onlineauctions.annotation.Permission)")
    private void withinPointCut() {
    }


    /**
     * 定义一个私有的类点切口，
     * 用于在具有特定注解但不在特定类中的类上应用。
     */
    @Pointcut("withinPointCut() &&!annotationPointCut()")
    private void classPointCut() {

    }


    /**
     * 用于在具有特定注解但不在特定类中的方法上应用。
     */
    @Pointcut("!withinPointCut() && annotationPointCut()")
    private void methodPointCut() {
    }


    /**
     * 定义一个私有的注解点切口，用于在类和方法上应用。
     */
    @Pointcut("withinPointCut() && annotationPointCut()")
    private void classAndMethodPointCut() {
    }




    /**
     * 只有方法有注解执行
     *
     * @param point 函数调用的 ProceedingJoinPoint 对象
     * @return 方法的返回值
     * @throws Throwable 异常信息
     */
    @Around("methodPointCut()")
    public Object methodCheck(ProceedingJoinPoint point) throws Throwable{


        Role[] value = getMethodValues(point);
        String token = getToken();

        // 没有权限，返回权限失败的结果
        return (checkPermission(value,token))? point.proceed() : Result.codeFailure(ResultCode.ACCESS_DENIED);
    }

    /**
     * 只有类有注解 执行
     *
     * @param point ProceedingJoinPoint对象，表示当前的方法调用点
     * @return 返回方法的执行结果
     * @throws Throwable 异常信息
     */
    @Around("classPointCut())")
    public Object classCheck(ProceedingJoinPoint point) throws Throwable{

        Role[] value = getClassValues(point);
        String token = getToken();

        return (checkPermission(value,token))? point.proceed() : Result.codeFailure(ResultCode.ACCESS_DENIED);
    }


    /**
     * 类和方法都有注解执行
     *
     * @param point 执行的JoinPoint对象
     * @return 切面方法的返回值
     * @throws Throwable 可抛出的异常
     */
    @Around("classAndMethodPointCut()")
    public Object classAndMethodCheck(ProceedingJoinPoint point) throws Throwable {
        // 获取类和方法的注解值
        Role[] roles = getClassAndMethodValue(point);
        // 获取令牌
        String token = getToken();
        // 检查权限
        return (checkPermission(roles,token))? point.proceed() : Result.codeFailure(ResultCode.ACCESS_DENIED);
    }


    /**
     * 获取类和方法的权限值
     *
     * @param point 方法连接点
     * @return 权限值数组
     * @throws Throwable 异常
     */
    private Role[] getClassAndMethodValue(ProceedingJoinPoint point) throws Throwable {
        //若类 的 isIndividual()为true，则方法中的isIndividual()就不用获取
        boolean flag = false;
        // 获取类上的Permission注解
        Permission classAnnotation = point.getTarget().getClass().getAnnotation(Permission.class);

        Set<Role> roles = new HashSet<>();

        if (!classAnnotation.isIndividual()) {
            // 如果类上的Permission注解为isIndividual() false，则获取类上的Permission注解中的角色
            Role[] values = classAnnotation.value();
            roles = Arrays.stream(values).collect(Collectors.toSet());
        }else {
            flag = true;
        }

        // TODO 到时候将类注解相关的进行操作

        // 获取方法对象
        Method method = getMethod(point);
        // 获取方法上的Permission注解
        Permission methodAnnotation = method.getAnnotation(Permission.class);

        if (flag || methodAnnotation.isIndividual()) {
            // 如果方法上的Permission注解为isIndividual() true 或者类的isIndividual() true ，则清空角色集合
            roles.clear();
        }

        if (methodAnnotation.isAllowAll()) {
            // 如果方法上的Permission注解为isAllowAll() true ，则返回所有role
            return Role.values();
        }

        // 如果都没有限制admin，admin就有权限
        if (classAnnotation.isAllowAdmin() && methodAnnotation.isAllowAdmin()){
            roles.add(Role.ADMIN);
        }

        // 将方法上的Permission注解中的角色添加到角色集合中
        Collections.addAll(roles, methodAnnotation.value());
        // 将角色集合转换为数组
        Role[] value = roles.toArray(new Role[0]);
        // 返回角色数组
        return value;
    }


    /**
     * 获取Method对象
     *
     * @param point ProceedingJoinPoint
     * @return Method
     */
    private Method getMethod(ProceedingJoinPoint point) {
        // 将point的签名转换为MethodSignature类型
        MethodSignature signature = (MethodSignature) point.getSignature();
        // 返回Method对象
        return signature.getMethod();
    }

    /**
     * 获取方法上的Permission注解的value属性值
     *
     * @param point ProceedingJoinPoint
     * @return Role[]
     *
     */
    private Role[] getMethodValues(ProceedingJoinPoint point) {
        // 获取方法对象
        Method method = getMethod(point);
        // 获取方法上的Permission注解
        Permission annotation = method.getAnnotation(Permission.class);
        Role[] value = annotation.value();
        if (annotation.isAllowAll()) {
            // 如果方法上的Permission注解为isAllowAll() true ，则返回所有role
            return Role.values();
        }
        if (annotation.isAllowAdmin()){
            //若没有定义管理员不能访问，就添加管理员
            value = addRole(value,Role.ADMIN);
        }
        // 返回Permission注解的value属性值
        return value;
    }


    /**
     * 获取指定 ProceedingJoinPoint 对象所属类的 Permission 注解的 value 数组
     *
     * @param point ProceedingJoinPoint 对象，表示当前正在执行的方法调用点
     * @return Permission 注解的 value 数组
     */
    private Role[] getClassValues(ProceedingJoinPoint point) {
        // 获取 point 所属类的 Permission 注解
        Permission annotation = point.getTarget().getClass().getAnnotation(Permission.class);
        Role[] value = annotation.value();
        if (annotation.isAllowAll()) {
            // 如果方法上的Permission注解为isAllowAll() true ，则返回所有role
            return Role.values();
        }
        if (annotation.isAllowAdmin()){
            //若没有定义管理员不能访问，就添加管理员
            value = addRole(value,Role.ADMIN);
        }
        // 返回 Permission 注解的 value 数组
        return value;
    }

    /**
     * 检查权限方法
     *
     * @param roles 权限注解中的角色类型数组
     * @param token 令牌
     * @return 如果请求用户的角色与数组中任意一个角色匹配，则返回true；否则返回false
     */
    private boolean checkPermission(Role[] roles,String token) {

        // 通过token获取用户的角色
        Integer role = JwtUtil.getRole(token);

        // 遍历权限注解中的角色类型数组
        return Role.checkRole(roles,role);
    }

    /**
     * 从header中获取token
     *
     * @return token
     */
    private String getToken(){
        // 将请求包装成Servlet请求属性，获取请求对象
        HttpServletRequest request = getRequest();
        // 获取请求头中的Token
        String token = request.getHeader("token");
        return token;
    }


    /**
     * 获取HttpServletRequest对象
     *
     * @return HttpServletRequest对象
     */
    private HttpServletRequest getRequest(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return request;
    }


    /**
     * 向角色数组中添加角色
     *
     * @param roles 角色数组
     * @param role 待添加的角色
     *
     * @return 添加角色后的角色数组
     */
    private Role[] addRole(Role[] roles, Role role) {

        int length = roles.length;
        // 创建一个新的角色数组，长度为原数组长度加1
        Role[] newRole = new Role[length + 1];
        // 将原角色数组复制到新数组中
        System.arraycopy(roles, 0, newRole, 0, length);
        // 将待添加的角色放入新数组的最后一个位置
        newRole[length] = role;
        // 将新数组赋值给原数组，实现角色的添加
        return newRole;
    }


}
