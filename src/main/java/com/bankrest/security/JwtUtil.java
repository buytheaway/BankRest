package com.bankrest.security;
import io.jsonwebtoken.*; import io.jsonwebtoken.security.Keys; import java.security.Key; import java.util.Date;
public class JwtUtil {
  private final Key key; private final long ttlMs;
  public JwtUtil(String secret, long ttlMs){ this.key=Keys.hmacShaKeyFor(java.util.Arrays.copyOf(secret.getBytes(), 64)); this.ttlMs=ttlMs; }
  public String generate(String username, String role){
    long now=System.currentTimeMillis();
    return Jwts.builder().subject(username).claim("role", role).issuedAt(new Date(now)).expiration(new Date(now+ttlMs)).signWith(key, SignatureAlgorithm.HS256).compact();
  }
  public Jws<Claims> parse(String token){ return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); }
}
