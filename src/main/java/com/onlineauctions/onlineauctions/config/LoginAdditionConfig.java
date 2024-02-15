package com.onlineauctions.onlineauctions.config;

import com.onlineauctions.onlineauctions.annotation.Permission;
import com.onlineauctions.onlineauctions.pojo.config.NoLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class LoginAdditionConfig implements ApplicationRunner {
    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private LoginConfig loginConfig;

    /**
     * 重写ApplicationArguments接口的run方法。
     * 从应用上下文中获取RequestMappingHandlerMapping bean。
     * 遍历包含URL与类和方法映射关系的集合。
     * 检查类或方法是否标注了Permission注解。
     * 如果未标注，则将URL模式添加到无需登录（noLogin）集合中。
     * 最后打印出无需登录集合中的内容。
     *
     * @param args 应用程序参数对象
     * @throws Exception 如果运行过程中发生异常
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {

        // 获取RequestMapping处理器映射实例
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        // 获取URL与处理方法的映射信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            RequestMappingInfo info = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();

            // 获取当前方法所在的类
            Class<?> type = handlerMethod.getBeanType();
            // 检查类上是否有@Permission注解，如果有则跳过该类的所有方法
            Permission typeAnnotation = type.getAnnotation(Permission.class);
            if (typeAnnotation != null) continue;

            // 获取当前处理方法
            Method method = handlerMethod.getMethod();
            // 检查方法上是否有@Permission注解，如果有则跳过该方法
            Permission methodAnnotation = method.getAnnotation(Permission.class);
            if (methodAnnotation != null) continue;

            // 获取并更新无需登录配置的相关集合
            Set<String> path = loginConfig.getNoLogin().getPath();
            Set<String> contains = loginConfig.getNoLogin().getContains();

            PatternsRequestCondition patternsCondition = info.getPatternsCondition();
            for (String pattern : patternsCondition.getPatterns()) {
                if (containsBraces(pattern)) {
                    contains.add(pattern);
                }
                path.add(pattern);
            }

            // 创建新的NoLogin实例，并设置路径和包含条件
            NoLogin noLogin = new NoLogin();
            noLogin.setPath(path);
            noLogin.setContains(contains);
            // 更新全局的无需登录配置
            loginConfig.setNoLogin(noLogin);
        }

    }

    public boolean containsBraces(String input) {
        Pattern pattern = Pattern.compile("\\{.*\\}");
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }
}
