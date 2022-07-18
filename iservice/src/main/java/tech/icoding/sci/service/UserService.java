package tech.icoding.sci.service;

import java.lang.Long;
import org.springframework.stereotype.Service;
import tech.icoding.sci.entity.UserEntity;
import tech.icoding.sci.repository.UserRepository;

@Service
public class UserService extends BaseService<UserRepository, UserEntity, Long> {
}
