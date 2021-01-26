package tech.icoding.sci.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.icoding.sci.entity.UserEntity;

/**
 * @author : Joe
 * @date : 2021/1/11
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
