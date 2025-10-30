package com.bankrest.security;
import com.bankrest.entity.UserEntity; import com.bankrest.repo.UserRepository;
import jakarta.servlet.*; import jakarta.servlet.http.*; import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority; import org.springframework.security.core.context.SecurityContextHolder; import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException; import java.util.List;
public class JwtAuthFilter extends OncePerRequestFilter {
  private final JwtUtil jwt; private final UserRepository users;
  public JwtAuthFilter(JwtUtil jwt, UserRepository users){ this.jwt=jwt; this.users=users; }
  @Override protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
    String h=req.getHeader("Authorization");
    if(h!=null && h.startsWith("Bearer ")){
      try{
        var j=jwt.parse(h.substring(7)); String username=j.getBody().getSubject(); String role=(String) j.getBody().get("role");
        UserEntity u=users.findByUsername(username).orElse(null);
        if(u!=null && u.isEnabled()){
          var auth=new UsernamePasswordAuthenticationToken(username, null, List.of(new SimpleGrantedAuthority("ROLE_"+role)));
          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      }catch(Exception ignored){}
    }
    chain.doFilter(req,res);
  }
}
