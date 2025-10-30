package com.bankrest.security;
import io.jsonwebtoken.*; import io.jsonwebtoken.security.Keys;
import java.security.Key; import java.util.Date; import java.util.Arrays; import java.nio.charset.StandardCharsets;
public class JwtUtil {
  private final Key key; private final long ttlMs;
  public JwtUtil(String secret, long ttlMs){
    this.key = Keys.hmacShaKeyFor(Arrays.copyOf(secret.getBytes(StandardCharsets.UTF_8), 64)); this.ttlMs=ttlMs;
  }
  public String generate(String username, String role){
    long now=System.currentTimeMillis();
    return Jwts.builder()
      .setSubject(username)
      .claim("role", role)
      .setIssuedAt(new Date(now))
      .setExpiration(new Date(now+ttlMs))
      .signWith(key, SignatureAlgorithm.HS256)
      .compact();
  }
  public Jws<Claims> parse(String token){
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
  }
}