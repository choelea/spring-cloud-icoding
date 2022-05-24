package tech.icoding.sci.service;

import java.lang.Long;
import org.springframework.stereotype.Service;
import tech.icoding.sci.entity.User;
import tech.icoding.sci.repository.UserRepository;

@Service
public class UserService extends BaseService<UserRepository, User, Long> {
}
