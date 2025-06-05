package org.pamdesa.helper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.pamdesa.model.properties.AuthProperties;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtHelper {

  private final AuthProperties authProperties;

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(authProperties.getSecretKey().getBytes());
  }

  public String generateToken(String username) {
    return Jwts.builder().setSubject(username).setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + authProperties.getTokenExpirationTime()))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
  }

  public String extractUsername(String token) {
    return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
        .getBody().getSubject();
  }

  public boolean validateToken(String token, String username) {
    return username.equals(extractUsername(token)) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    Date expiration =
        Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody()
            .getExpiration();
    return expiration.before(new Date());
  }

  // Extract claims from the token
  public Claims extractAllClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
        .getBody();
  }

  // Extract the expiration time from the token
  public Long getTokenExpiration(String token) {
    return extractAllClaims(token).getExpiration().getTime();
  }

}

