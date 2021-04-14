package gg.jominsubyungsin.domain.repository;

<<<<<<< HEAD
import gg.jominsubyungsin.domain.entitiy.UserEntity;
=======
import gg.jominsubyungsin.domain.entity.UserEntity;
>>>>>>> 4d3a949bb1041a05d5ac86f234cce4a4be9c6e7e
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
  Optional<UserEntity> findByEmail(String email);
  Optional<UserEntity> findById(Long Id);
  void deleteByEmail(String email);
  Optional<UserEntity> findByEmailAndPassword(String email, String password);
}
