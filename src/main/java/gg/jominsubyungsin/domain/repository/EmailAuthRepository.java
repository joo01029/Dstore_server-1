package gg.jominsubyungsin.domain.repository;

import gg.jominsubyungsin.domain.entity.EmailAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailAuthRepository extends JpaRepository<EmailAuthEntity, Long> {
  Optional<EmailAuthEntity> findByEmail(String email);
  Optional<EmailAuthEntity> findByCode(String code);
  Optional<EmailAuthEntity> findByEmailAndAuth(String email, Boolean auth);
}
