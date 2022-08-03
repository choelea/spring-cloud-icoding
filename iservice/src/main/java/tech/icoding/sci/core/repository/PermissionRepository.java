package tech.icoding.sci.core.repository;

import java.lang.Long;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tech.icoding.sci.core.entity.PermissionEntity;

@Repository
public interface PermissionRepository extends JpaSpecificationExecutor<Long>, JpaRepository<PermissionEntity, Long> {
}
