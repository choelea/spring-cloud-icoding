package tech.icoding.sci.core.service;

import java.lang.Long;
import org.springframework.stereotype.Service;
import tech.icoding.sci.core.entity.UserEntity;
import tech.icoding.sci.core.repository.UserRepository;

@Service
public class UserService extends BaseService<UserRepository, UserEntity, Long> {
}
