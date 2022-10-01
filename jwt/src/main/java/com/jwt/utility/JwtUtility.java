package com.jwt.utility;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtility implements Serializable {

    private static final long serialVersionUID = 143L;

    public static final long TOKEN_VALIDITY = 24 * 60 * 60 * 1000; // 24 hours

    @Value("${secret}")
    private String secretKey;

    private String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    public String generateTokenForUser(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>();
        return generateToken(claims, userDetails.getUsername());
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, (Claims) -> (Claims.getSubject()));
    }

    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, (Claims) -> (Claims.getExpiration()));
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }


    private Boolean isTokenExpired(String token){
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


    public Boolean validateUserToken(String token,UserDetails userDetails){
        String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
