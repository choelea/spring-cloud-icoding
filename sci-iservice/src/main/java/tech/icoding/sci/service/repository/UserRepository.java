package tech.icoding.sci.service.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tech.icoding.sci.service.entity.User;


@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
	
}