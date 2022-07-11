package com.serookie.auth.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/7/5
 */
@Component
@Slf4j
public class TokenManager {
    //后端的签名
    public static final String SECRET_KEY="kevintam#521205#";
    //生成一个token
    public String createToken(String username,Long uid){
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE,7);
        String token = Jwts.builder().setSubject(username)
                .setId(uid.toString())
                .setExpiration(instance.getTime())
                //加密
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
        return token;
    }
    //从token中拿出信息
    public Claims getClaimByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            log.info("validate is token error :{}", e);
            return null;
        }
    }
    /**
     * token是否过期
     * @return  true：过期
     */
    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }
}
