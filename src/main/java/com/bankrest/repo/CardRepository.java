package com.bankrest.repo;
import com.bankrest.entity.CardEntity; import com.bankrest.entity.UserEntity;
import org.springframework.data.domain.Page; import org.springframework.data.domain.Pageable; import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface CardRepository extends JpaRepository<CardEntity, Long> {
  Page<CardEntity> findByOwnerAndNumberEncContainingIgnoreCase(UserEntity owner, String q, Pageable pageable);
  Page<CardEntity> findByOwner(UserEntity owner, Pageable pageable);
  Optional<CardEntity> findByIdAndOwnerId(Long id, Long ownerId);
}
