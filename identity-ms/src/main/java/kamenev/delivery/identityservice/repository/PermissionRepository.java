package kamenev.delivery.identityservice.repository;

import kamenev.delivery.identityservice.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {
}
