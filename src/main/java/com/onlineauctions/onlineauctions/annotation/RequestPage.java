package com.onlineauctions.onlineauctions.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对于分页的相关注解 传入参数是定义默认值
 *
 * @apiNote    page 页码
 * limit 每页数量
 *
 * @author Clusters_stars
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface RequestPage {
    /**
     * 页码
     * @return int
     */
    int page() default 1;

    /**
     * 每页数量
     * @return int
     */
    int size() default 10;

}
