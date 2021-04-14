package gg.jominsubyungsin.domain.repository;

import gg.jominsubyungsin.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
  Optional<UserEntity> findByEmail(String email);
  Optional<UserEntity> findById(Long Id);
  void deleteByEmail(String email);
  Optional<UserEntity> findByEmailAndPassword(String email, String password);
}
