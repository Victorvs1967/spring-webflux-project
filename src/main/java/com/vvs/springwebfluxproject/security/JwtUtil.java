package com.vvs.springwebfluxproject.security;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Mono;

import static com.vvs.springwebfluxproject.util.ListUtil.toSingleton;

@Component
public class JwtUtil {
  
  @Value("${jjwt.secret}")
  private String secret;
  
  @Value("${jjwt.expiration}")
  private int expiration;

  private Key key;
  public static final String KEY_ROLE = "role";

  @PostConstruct
  public void init() {
    key = Keys.hmacShaKeyFor(secret.getBytes());
  }

  private Key getKey() {
    return key == null ? Keys.hmacShaKeyFor(secret.getBytes()) : key;
  }

  public Mono<Claims> getAllClaimsFromToken(String token) {
    return Mono.just(Jwts
      .parserBuilder()
      .setSigningKey(key)
      .build()
      .parseClaimsJws(token)
      .getBody());
  }

  public Mono<Boolean> validateToken(String token) {
    return getAllClaimsFromToken(token)
      .map(Claims::getExpiration)
      .map(expiration -> expiration.after(new Date()));
  }

  public String generateToke(UserDetails userDetails) {
    String authority = userDetails
      .getAuthorities().stream()
      .map(GrantedAuthority::getAuthority)
      .collect(toSingleton());

    Map<String, String> claims = new HashMap<>();
    claims.put(KEY_ROLE, authority);
      
    return Jwts
      .builder()
      .setClaims(claims)
      .setSubject(userDetails.getUsername())
      .setExpiration(Date.from(Instant.now().plus((Duration.ofHours(expiration)))))
      .setIssuedAt(Date.from(Instant.now()))
      .signWith(getKey())
      .compact();
  }

}
