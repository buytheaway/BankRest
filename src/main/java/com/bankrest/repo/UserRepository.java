package com.bankrest.repo;
import com.bankrest.entity.UserEntity; import java.util.Optional; import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<UserEntity, Long> { Optional<UserEntity> findByUsername(String username); }
