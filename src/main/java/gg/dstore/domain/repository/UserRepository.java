package gg.dstore.domain.repository;

import gg.dstore.domain.entity.UserEntity;

import gg.dstore.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByEmail(String email);
	Optional<UserEntity> findByEmailAndOnDelete(String email, Boolean onDelete);

	Optional<UserEntity> findByIdAndOnDelete(Long Id, Boolean onDelete);

	Optional<UserEntity> findByEmailAndPasswordAndOnDelete(String email, String password, Boolean onDelete);

	@Query("SELECT u FROM UserEntity as u WHERE u.name like %:name% and u.email not like :email and u.onDelete = false")
	List<UserEntity> findByNameLike(@Param("name") String name, @Param("email") String email);

	List<UserEntity> findByRole(Role admin);
}
