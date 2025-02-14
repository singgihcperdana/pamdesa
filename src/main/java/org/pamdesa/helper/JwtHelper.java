package org.pamdesa.helper;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtHelper {

  private static final String SECRET_KEY =
      "======================BezKoder=Spring===========================";
  private static final long EXPIRATION_TIME = 86400000; // 24 jam

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
  }

  public String generateToken(String username) {
    return Jwts.builder().setSubject(username).setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
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

