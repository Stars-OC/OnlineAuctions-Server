package com.onlineauctions.onlineauctions.annotation;

import com.onlineauctions.onlineauctions.pojo.type.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对于权限的相关注解
 * <p>
 * @apiNote    value 角色 Role[] 枚举类型的数组
 * isIndividual  是否是个体
 * (类注解下将此设为true将不受全类角色的影响)
 * isAllowAdmin 是否允许管理员访问 默认管理员可以访问该类
 *
 * @author Clusters_stars
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface Permission {

    /**
     * 角色控制
     * @return Role[]
     */
    Role[] value() default {Role.USER};

    /**
     * 是否是个体
     * @return boolean
     */
    boolean isIndividual() default false;

    /**
     * 是否允许管理员访问
     * @return boolean
     */
    boolean isAllowAdmin() default true;

    /**
     * 是否允许所有人访问(只能单类/单方法注解有效)
     * 若单类 多方法需要在方法进行设置
     * @return boolean
     */
    boolean isAllowAll() default false;

}
