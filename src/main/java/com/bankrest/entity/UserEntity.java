package com.bankrest.entity;
import com.bankrest.domain.UserRole; import jakarta.persistence.*; import lombok.*;
@Entity @Table(name="app_user")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserEntity {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(unique=true, nullable=false) private String username;
  @Column(nullable=false) private String password;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private UserRole role;
  @Column(nullable=false) private boolean enabled=true;
}
