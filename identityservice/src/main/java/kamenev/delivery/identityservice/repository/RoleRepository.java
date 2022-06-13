package kamenev.delivery.identityservice.repository;

import kamenev.delivery.identityservice.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Role getByName(String name);
}
