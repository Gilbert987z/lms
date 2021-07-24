package cn.zjut.lms;

import io.jsonwebtoken.*;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

public class jsonwebtoken {
    private long time = 1000 * 60 * 60 * 24;
    private String signature = "admin";

    @Test
    public void jwt() {
        JwtBuilder jwtBuilder = Jwts.builder();
        String jwtToken = jwtBuilder
                //header
                .setHeaderParam("typ", "Jwt")
                .setHeaderParam("role", "HS256")
                //payload
                .claim("username", "tom")
                .claim("role", "admin")
                .setSubject("admin_test")
                .setExpiration(new Date(System.currentTimeMillis() + time))
                .setId(UUID.randomUUID().toString())
                //signature
                .signWith(SignatureAlgorithm.HS256, signature)
                .compact();
    }
    @Test
    public void parse(){
        String token ="";
        JwtParser jwtParser=Jwts.parser();
        Jws<Claims> claimsJws = jwtParser.setSigningKey(signature).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        System.out.println(claims.get("username"));
        System.out.println(claims.get("role"));
        System.out.println(claims.getId());
        System.out.println(claims.getSubject());
        System.out.println(claims.getExpiration());
    }
}
