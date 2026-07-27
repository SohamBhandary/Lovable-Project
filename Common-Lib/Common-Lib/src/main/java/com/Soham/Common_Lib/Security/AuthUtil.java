package com.Soham.Common_Lib.Security;


import com.Soham.Common_Lib.DTOs.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

@Component
public class AuthUtil {

    @Value("${jwt.secretKey}")
    private String jwtKey;

    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));
    }


    public String generateAccessToken(JWTUserprincipal user){
        return Jwts.builder().setSubject(user.username())
                .claim("userId",user.userId().toString())
                .claim("name", user.name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*60*1000))
                .signWith(getSecretKey())
                .compact();
    }

    public JWTUserprincipal verifyAccessToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build().parseSignedClaims(token).getPayload();

        Long userId = Long.parseLong(claims.get("userId", String.class));
        String name = claims.get("name", String.class);
        String username = claims.getSubject();

        return new JWTUserprincipal(userId, username, username, null, new ArrayList<>());
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !(authentication.getPrincipal() instanceof JWTUserprincipal userPrincipal)) {
            throw new AuthenticationCredentialsNotFoundException("No JWT Found");
        }
        return userPrincipal.userId();
    }

}
