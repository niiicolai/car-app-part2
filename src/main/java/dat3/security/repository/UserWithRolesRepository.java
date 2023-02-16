package dat3.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dat3.security.entity.UserWithRoles;

@Repository
public interface UserWithRolesRepository extends JpaRepository<UserWithRoles, String> {
}
