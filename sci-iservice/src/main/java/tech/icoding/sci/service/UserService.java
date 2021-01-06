package tech.icoding.sci.service;

import org.springframework.stereotype.Service;
import tech.icoding.sci.service.entity.User;
import tech.icoding.sci.service.repository.UserRepository;

/**
 * @author : Joe
 * @date : 2021/1/6
 */
@Service
public class UserService extends BaseService<UserRepository, User, Long>{

}
