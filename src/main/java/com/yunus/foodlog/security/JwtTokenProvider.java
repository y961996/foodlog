package com.yunus.foodlog.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${foodlog.app.secret}")
    private String APP_SECRET;

    @Value("${foodlog.expires.in}")
    private Long EXPIRES_IN;

    private SecretKey createSecretKeyFromAppSecret() {
        return Keys.hmacShaKeyFor(APP_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    private Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(createSecretKeyFromAppSecret())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateJwtToken(Authentication authentication) {
        JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
        Date expireDate = new Date(new Date().getTime() + EXPIRES_IN);

        return Jwts.builder()
                .setSubject(Long.toString(userDetails.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(createSecretKeyFromAppSecret())
                .compact();
    }

    public String generateJwtTokenById(Long userId) {
        Date expireDate = new Date(new Date().getTime() + EXPIRES_IN);
        return Jwts.
                builder().
                setSubject(Long.toString(userId)).
                setIssuedAt(new Date()).
                setExpiration(expireDate).
                signWith(createSecretKeyFromAppSecret()).
                compact();
    }

    public Long getUserIdFromJwt(String token) {
        return Long.parseLong(parseToken(token).getSubject());
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return !isTokenExpired(token);
        } catch(SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = parseToken(token).getExpiration();
        return expiration.before(new Date());
    }
}
