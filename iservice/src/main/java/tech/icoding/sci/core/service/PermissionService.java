package tech.icoding.sci.core.service;

import java.lang.Long;
import org.springframework.stereotype.Service;
import tech.icoding.sci.core.entity.PermissionEntity;
import tech.icoding.sci.core.repository.PermissionRepository;

@Service
public class PermissionService extends BaseService<PermissionRepository, PermissionEntity, Long> {
}
