package com.onlineauctions.onlineauctions.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineauctions.onlineauctions.config.LoginConfig;
import com.onlineauctions.onlineauctions.pojo.respond.Result;
import com.onlineauctions.onlineauctions.pojo.type.ResultCode;
import com.onlineauctions.onlineauctions.service.redis.JwtService;
import com.onlineauctions.onlineauctions.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@WebFilter(urlPatterns = "/*")
public class LoginFilter implements Filter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private LoginConfig loginConfig;

    private final static ObjectMapper mapper = new ObjectMapper();

    /**
     * 过滤器方法，用于处理请求和响应
     *
     * @param servletRequest  Servlet请求对象
     * @param servletResponse Servlet响应对象
     * @param chain           过滤器链
     * @throws IOException      如果处理过程中发生I/O错误
     * @throws ServletException 如果处理过程中发生Servlet错误
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

        // 将ServletRequest和ServletResponse转换为HttpServletRequest和HttpServletResponse
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 获取请求的URL
        String url = request.getRequestURI();

        // 根据配置文件放行端口
        if (loginConfig.getNoLogin().getPath().contains(url) || match(url)) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }
        log.info("拦截请求：{}", url);
        //CORS域 配置 前端已设置
//        response.addHeader("Access-Control-Allow-Origin", "*");
//        response.addHeader("Access-Control-Allow-Methods", "GET, POST");
//        response.addHeader("Access-Control-Allow-Headers", "Content-Type,token");

        // 获取请求头中的token
        String token = request.getHeader("token");
        response.setContentType("text/plain; charset=utf-8");
        ResultCode resultCode = null;
        try {

            // 使用JwtUtil工具类获取token中的claims信息
            Claims claims = JwtUtil.getClaims(token);

            // 如果claims信息为空，则返回"token不存在，请登录"

            String username = String.valueOf(claims.get("username"));

            String oldToken = jwtService.getJwtByUsername(username);

            if (oldToken.equals(token)) {
                // 放行请求，继续处理
                chain.doFilter(servletRequest, servletResponse);
                return;
            }

            resultCode = ResultCode.TOKEN_EXPIRED;

        } catch (JwtException | IllegalArgumentException | StringIndexOutOfBoundsException | NullPointerException e) {
            String errorMsg = e.getMessage();

            log.warn("Token验证失败 : {}", errorMsg);

            // 如果抛出异常类型为StringIndexOutOfBoundsException，则将错误信息设置为"Token错误"
            if (e instanceof StringIndexOutOfBoundsException) {
                resultCode = ResultCode.TOKEN_INVALID;
            // 如果抛出异常类型为"expired"，则将错误信息设置为"Token已过期"
            } else if (errorMsg.contains("expired")) {
                resultCode = ResultCode.TOKEN_EXPIRED;
            // 如果抛出异常类型为"signature"，则将错误信息设置为"签名不匹配"
            } else if (errorMsg.contains("signature")) {
                resultCode = ResultCode.TOKEN_SIGN_ERROR;
            } else if (e instanceof NullPointerException | e instanceof IllegalArgumentException){
                resultCode = ResultCode.INVALID_TOKEN;
            }else {
                resultCode = ResultCode.ACCESS_DENIED;
            }


        }
        String result = mapper.writeValueAsString(Result.codeFailure(resultCode));
        // 返回错误信息
        response.getWriter().write(result);
        response.setStatus(401);

    }

    private boolean contains(String url){
        Set<String> contains = loginConfig.getNoLogin().getContains();
        for (String contain : contains) {
            if (url.startsWith(contain)) return true;
        }
        return false;
    }

    /**
     * 判断给定的URL是否匹配配置中的不允许登录的URL
     *
     * @param url 待判断的URL
     * @return 如果URL匹配不允许登录的URL则返回true，否则返回false
     */
    private boolean match(String url){
        // 获取不允许登录的URL集合
        Set<String> contains = loginConfig.getNoLogin().getContains();

        // 遍历不允许登录的URL集合
        for (String contain : contains) {
            // 替换URL中的占位符
            String regex = contain.replaceAll("\\{.*?}", "(\\\\w+)");
            // 编译正则表达式
            Pattern pattern = Pattern.compile(regex);
            // 创建匹配器
            Matcher matcher = pattern.matcher(url);
            // 如果匹配成功则返回true
            if (matcher.find()) return true;
        }

        // 如果没有匹配成功则返回false
        return false;
    }

}


