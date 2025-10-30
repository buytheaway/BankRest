package com.bankrest.service;
import com.bankrest.domain.UserRole; import com.bankrest.entity.UserEntity; import com.bankrest.repo.UserRepository;
import org.springframework.boot.CommandLineRunner; import org.springframework.stereotype.Component; import org.springframework.security.crypto.password.PasswordEncoder;
@Component
public class InitAdminRunner implements CommandLineRunner {
  private final UserRepository repo; private final PasswordEncoder enc;
  public InitAdminRunner(UserRepository r, PasswordEncoder e){ this.repo=r; this.enc=e; }
  @Override public void run(String... args){
    String u=System.getenv().getOrDefault("ADMIN_USERNAME","admin");
    String p=System.getenv().getOrDefault("ADMIN_PASSWORD","admin123");
    if(repo.findByUsername(u).isEmpty()){
      repo.save(UserEntity.builder().username(u).password(enc.encode(p)).role(UserRole.ADMIN).enabled(true).build());
    }
  }
}
