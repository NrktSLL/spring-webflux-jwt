package com.nrkt.springwebfluxjwtex.security.util.jwt;

import com.nrkt.springwebfluxjwtex.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.jackson.io.JacksonSerializer;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.time.Duration;
import java.util.AbstractMap;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration.ms}")
    private Long jwtExpirationMs;

    @Value("${jwt.issuer}")
    private String issuer;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    private JwtParser jwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
    }

    public Claims getClaimsFromToken(String token) {
        try {
            return jwtParser().parseClaimsJws(token).getBody();
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return Jwts.claims();
    }

    public Boolean validateJwtToken(String token) {
        var exitToken = redisTemplate.opsForValue().get(getId(token));
        if (exitToken == null) return false;

        return !getClaimsFromToken(token).isEmpty();
    }

    public String getSubject(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public String getId(String token) {
        return getClaimsFromToken(token).getId();
    }

    public Date getExpirationDate(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    public String generateJwtToken(User user) {
        Assert.notNull(user, "user can not be null");

        var exitToken = redisTemplate.opsForValue().get(user.getId());
        if (exitToken != null) {
            if (Boolean.FALSE.equals(validateJwtToken(exitToken))) {
                redisTemplate.delete(user.getId());
            } else return exitToken;
        }

        final AbstractMap.SimpleImmutableEntry<String, Object> userRoles =
                new AbstractMap.SimpleImmutableEntry<>("role", user.getAuthorities());

        Map<String, Object> claims = new HashMap<>();
        claims.put(userRoles.getKey(), userRoles.getValue());

        final var createdDate = new Date();
        final var expirationDate = new Date(createdDate.getTime() + jwtExpirationMs);

        var token = Jwts.builder()
                .setId(user.getUsername())
                .setSubject(user.getEmail())
                .setIssuer(issuer)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .addClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512)
                .serializeToJsonWith(new JacksonSerializer<>())
                .compact();

        var duration = Duration.between(createdDate.toInstant(), expirationDate.toInstant());

        redisTemplate.opsForValue().set(user.getUsername(), token, duration);

        return token;
    }
}
