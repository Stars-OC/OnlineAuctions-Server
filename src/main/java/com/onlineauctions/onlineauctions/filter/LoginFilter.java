package com.onlineauctions.onlineauctions.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineauctions.onlineauctions.pojo.Result;
import com.onlineauctions.onlineauctions.pojo.type.ResultCode;
import com.onlineauctions.onlineauctions.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/*")
public class LoginFilter implements Filter {

    @Autowired
    private StringRedisTemplate redis;

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
        String url = request.getRequestURL().toString();

        //TODO 后面利用list或者其他方法放行这些接口
        if (url.contains("/auth/login") || url.contains("/auth/register") || !url.contains("/api/")) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }

        //CORS域 配置
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type,token");

        // 获取请求头中的token
        String token = request.getHeader("token");
        response.setContentType("text/plain; charset=utf-8");
        ResultCode resultCode = null;
        try {

            // 使用JwtUtil工具类获取token中的claims信息
            Claims claims = JwtUtil.getClaims(token);

            // 如果claims信息为空，则返回"token不存在，请登录"

            String username = String.valueOf(claims.get("username"));

            String oldToken = redis.opsForValue().get(username);

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
}


