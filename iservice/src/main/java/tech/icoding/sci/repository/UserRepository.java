package tech.icoding.sci.repository;

import java.lang.Long;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tech.icoding.sci.entity.UserEntity;

@Repository
public interface UserRepository extends JpaSpecificationExecutor<Long>, JpaRepository<UserEntity, Long> {
}
