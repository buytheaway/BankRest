package com.bankrest.config;
import com.bankrest.repo.UserRepository; import com.bankrest.security.JwtAuthFilter; import com.bankrest.security.JwtUtil;
import org.springframework.context.annotation.*; import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver; import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity; import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.*; import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain; import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
@Configuration @EnableMethodSecurity
public class SecurityConfig {
  @Bean PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }
  @Bean UserDetailsService uds(UserRepository repo){
    return username -> repo.findByUsername(username)
      .map(u -> User.builder().username(u.getUsername()).password(u.getPassword()).roles(u.getRole().name()).disabled(!u.isEnabled()).build())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
  @Bean AuthenticationManager authManager(UserDetailsService uds, PasswordEncoder enc){
    var p=new DaoAuthenticationProvider(); p.setUserDetailsService(uds); p.setPasswordEncoder(enc); return new ProviderManager(p);
  }
  @Bean JwtUtil jwt(@Value("${app.jwt.secret:dev-jwt-secret-change-me}") String s, @Value("${app.jwt.ttl-ms:3600000}") long ttl){ return new JwtUtil(s, ttl); }
  @Bean SecurityFilterChain filterChain(HttpSecurity http, JwtUtil jwt, UserRepository users) throws Exception {
    http.csrf(cs->cs.disable())
      .sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(ar->ar
        .requestMatchers("/auth/**","/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html").permitAll()
        .requestMatchers("/api/admin/**").hasRole("ADMIN")
        .anyRequest().authenticated()
      )
      .addFilterBefore(new JwtAuthFilter(jwt, users), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
