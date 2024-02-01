package com.onlineauctions.onlineauctions.annotation;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(2)
public class VerifyTimeAspect {
}
