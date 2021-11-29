package cn.zjut.lms.util;

import cn.zjut.lms.model.User;
import io.jsonwebtoken.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
//@Data
//@Component
//@ConfigurationProperties(prefix = "jwt")
public class JwtUtil {

//    public static final long EXPIRE_TIME = 1000 * 60 * 30; //半小时过期
    public static final long EXPIRE_TIME = 1000 * 60; //半小时过期
    private static final String SECRET = "admin";
    private static final String SUBJECT = "LMS";

//    private long EXPIRE_TIME; //半小时过期
//    private String SECRET;
//    private String SUBJECT;

    /**
     * 根据负责生成JWT的token    generate
     */
    public static String generateToken(User user) {


        if (user == null || user.getId() == null || user.getUsername() == null) {
            return null;
        }

        JwtBuilder jwtBuilder = Jwts.builder();
        String token = jwtBuilder
                //header
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                //payload
                .claim("userId", user.getId())
                .claim("username", user.getUsername())
                //.claim("role", "admin")
                .setSubject(SUBJECT) //主题
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME)) //过期时间
                .setId(UUID.randomUUID().toString()) //jwt 编号
                //signature
                .signWith(SignatureAlgorithm.HS256, SECRET) //密钥
                .compact();
        return token;
    }

    /**
     * //校验token
     *
     * @param token
     * @return
     */
    public static boolean checkToken(String token) {
        if (token == null) {
            return false;
        }
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token); //解码操作
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public static String getUsernameFromToken(String token) {
        String username = null;
        try {
            Claims claims = getClaimsFromToken(token);

//            System.out.println(claims.get("username"));
            username = claims.get("username").toString();
//            String subject = claims.getSubject();
//            username = claims.getSubject();
        } catch (Exception e) {
            System.out.println("e = " + e.getMessage());
        }
        return username;
    }

    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public static Boolean isTokenExpired(String token) throws Exception {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            throw new Exception("签名过期");
        }
    }

    /**
     * 刷新令牌
     *
     * @param token 原令牌
     * @return 新令牌
     */
    public static String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put(Claims.ISSUED_AT, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 验证令牌
     *
     * @param token       令牌
     * @param userDetails 用户
     * @return 是否有效
     */
    public static Boolean validateToken(String token, UserDetails userDetails) throws Exception {
        User user = (User) userDetails;
        String username = getUsernameFromToken(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private static String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("typ", Header.JWT_TYPE);
        return Jwts.builder().setHeader(map).setClaims(claims).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private static Claims getClaimsFromToken(String token) throws Exception {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            new Throwable(e);
        }
        return claims;
    }

}
