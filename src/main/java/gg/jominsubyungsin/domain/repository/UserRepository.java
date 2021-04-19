package gg.jominsubyungsin.domain.repository;

import gg.jominsubyungsin.domain.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
  Optional<UserEntity> findByEmail(String email);

  Optional<UserEntity> findById(Long Id);
  void deleteByEmail(String email);
  Optional<UserEntity> findByEmailAndPassword(String email, String password);

  @Query("SELECT u FROM UserEntity as u WHERE u.name like %:name% and u.email not like :email")
  List<UserEntity> findByNameLike(@Param("name")String name, @Param("email")String email);
}
