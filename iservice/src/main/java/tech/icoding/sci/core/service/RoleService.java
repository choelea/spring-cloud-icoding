package tech.icoding.sci.core.service;

import java.lang.Long;
import org.springframework.stereotype.Service;
import tech.icoding.sci.core.entity.RoleEntity;
import tech.icoding.sci.core.repository.RoleRepository;

@Service
public class RoleService extends BaseService<RoleRepository, RoleEntity, Long> {
}
