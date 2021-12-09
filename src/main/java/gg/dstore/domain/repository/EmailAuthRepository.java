package gg.dstore.domain.repository;

import gg.dstore.domain.entity.EmailAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailAuthRepository extends JpaRepository<EmailAuthEntity, Long> {
	Optional<EmailAuthEntity> findByEmail(String email);

	Optional<EmailAuthEntity> findByCode(String code);

	Optional<EmailAuthEntity> findByEmailAndAuth(String email, Boolean auth);
}
