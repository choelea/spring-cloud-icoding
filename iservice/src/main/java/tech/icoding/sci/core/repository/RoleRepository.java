package tech.icoding.sci.core.repository;

import java.lang.Long;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tech.icoding.sci.core.entity.RoleEntity;

@Repository
public interface RoleRepository extends JpaSpecificationExecutor<Long>, JpaRepository<RoleEntity, Long> {
}
