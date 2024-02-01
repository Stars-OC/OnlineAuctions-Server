package com.onlineauctions.onlineauctions.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.onlineauctions.onlineauctions.utils.AesUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig implements MetaObjectHandler {

    /**
     * 分页插件配置
     * <p>
     * - 使用PaginationInnerInterceptor实现分页功能
     */
    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }


    /**
     * 插入填充操作
     * <p>将当前时间的秒数设置为指定字段的值
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("updateAt", System.currentTimeMillis()/1000, metaObject);
        this.setFieldValByName("createAt", System.currentTimeMillis()/1000, metaObject);
        // 将密码进行填充
        this.setFieldValByName("password", AesUtil.DEFAULT_PASSWORD , metaObject);
        // 将资源进行填充 直接存Json进数据库
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateAt", System.currentTimeMillis()/1000, metaObject);
    }


}