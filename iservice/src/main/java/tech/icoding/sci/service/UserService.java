package tech.icoding.sci.service;

import org.springframework.stereotype.Service;
import tech.icoding.sci.entity.UserEntity;
import tech.icoding.sci.repository.UserRepository;

/**
 * @author : Joe
 * @date : 2021/1/11
 */
@Service
public class UserService extends BaseService<UserRepository, UserEntity, Long>{
}
