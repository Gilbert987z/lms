package cn.zjut.lms.util;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.UUID;

public class JwtUtil {

    private static final long EXPIRE_TIME = 1000*60*30; //半小时过期
    private static final String TOKEN_SECRET = "admin";

    public static String createToken(String username){
        JwtBuilder jwtBuilder = Jwts.builder();
        String token = jwtBuilder
                //header
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                //payload
                .claim("username", username)
//                .claim("role", "admin")
                .setSubject("lms")
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRE_TIME))
                .setId(UUID.randomUUID().toString())
                //signature
                .signWith(SignatureAlgorithm.HS256, TOKEN_SECRET)
                .compact();
        return token;
    }

    public static boolean checkToken(String token){
        if(token == null){
            return false;
        }
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(TOKEN_SECRET).parseClaimsJws(token); //解码操作
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
