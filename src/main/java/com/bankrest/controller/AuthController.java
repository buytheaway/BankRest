package com.bankrest.controller;
import com.bankrest.dto.*; import com.bankrest.repo.UserRepository; import com.bankrest.security.JwtUtil;
import org.springframework.http.ResponseEntity; import org.springframework.security.authentication.AuthenticationManager; import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/auth")
public class AuthController {
  private final AuthenticationManager am; private final JwtUtil jwt; private final UserRepository users;
  public AuthController(AuthenticationManager am, JwtUtil jwt, UserRepository users){ this.am=am; this.jwt=jwt; this.users=users; }
  @PostMapping("/login")
  public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest req){
    try { am.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password())); }
    catch(AuthenticationException e){ return ResponseEntity.status(401).build(); }
    var role = users.findByUsername(req.username()).orElseThrow().getRole().name();
    return ResponseEntity.ok(new JwtResponse(jwt.generate(req.username(), role)));
  }
}
