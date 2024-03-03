package com.onlineauctions.onlineauctions.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineauctions.onlineauctions.pojo.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

@Slf4j
public class JwtUtil {

    //UUID.randomUUID().toString().replaceAll("-", "")
    private final static String SECRET = "aa50cd4006444e498defcc8ffc07c6ab";

    private final static ObjectMapper mapper = new ObjectMapper();

    private static SecretKey stringToSecretKey(String secret) {
        return new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * 创建JWTToken
     * @param user 用户信息
     * @param validTime 有效时间
     * @return JWTToken字符串
     */
    public static String createJwt(User user, Integer validTime) {
        long now = System.currentTimeMillis();
        long expire = now + validTime * 3600000L;

        return Jwts.builder()
                // 设置签发者
                .setIssuer("auctions")
                // 设置签发时间
                .setIssuedAt(new Date(now))
                // 设置过期时间
                .setExpiration(new Date(expire))
                // 添加用户名声明
                .claim("username", user.getUsername())
                // 添加昵称声明
                .claim("nickname", user.getNickname())
                // 添加头像声明
                .claim("avatarUrl", user.getAvatarUrl())
                .claim("description", user.getDescription())
                // 添加角色声明
                .claim("role", user.getRole())
                // 添加注册时间声明
                .claim("createAt",user.getCreateAt())
                // 使用SECRET作为签名密钥
                .signWith(stringToSecretKey(SECRET))
                .compact(); // 缩小JWTToken长度
    }


    /**
     * 验证并返回 JWT Body
     *
     * @param jwt 要验证的JWT
     * @return 解析出的Claims对象
     * @throws JwtException 如果解析过程中发生错误
     * @throws IllegalArgumentException 如果JWT无效
     */
    public static Claims getClaims(String jwt)
            throws JwtException, IllegalArgumentException {
        return Jwts.parserBuilder()
                .setSigningKey(stringToSecretKey(SECRET))
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }


    /**
     * 从 JWT 中取出 username
     * <p>此操作不验证 JWT 签名</p>
     *
     * @return username or null
     */
    public static Long getUsername(String jwt) {

        String payload = getPayload(jwt);

        try {
            JsonNode node = mapper.readTree(payload);
            return node.get("username").longValue();
        } catch (JacksonException e) {
            log.error("获取username失败从 token: {}",jwt);
            return null;
        }
    }

    /**
     * 获取角色权限
     * @param jwt JWT token
     * @return 角色ID，如果获取失败返回null
     */
    public static Integer getRole(String jwt) {

        String payload = getPayload(jwt);

        try {
            JsonNode node = mapper.readTree(payload);
            return node.get("role").intValue();
        } catch (JacksonException e) {
            log.error("获取role失败从 token: {}",jwt);
            return null;
        }
    }

    /**
     * 获取给定JWT的有效载荷。
     *
     * @param jwt JWT字符串
     * @return 有效载荷字符串
     */
    private static String getPayload(String jwt) {
        // JWT是Base64Url编码
        // 在第一个和最后一个'.'发生的位置切割JWT字符串，并从中间部分删除'-'和'_'字符
        String base64 = jwt.substring(jwt.indexOf('.') + 1, jwt.lastIndexOf('.'))
                .replaceAll("-", "+")
                .replaceAll("_", "/");

        // 解码Base64编码字符串并将其转换为字符串
        return new String(Base64.getDecoder().decode(base64));
    }


}
