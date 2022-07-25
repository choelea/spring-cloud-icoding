package tech.icoding.sci.facade.admin;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import tech.icoding.sci.core.entity.RoleEntity;
import tech.icoding.sci.core.service.RoleService;
import tech.icoding.sci.sdk.data.RoleData;
import tech.icoding.sci.sdk.form.admin.RoleForm;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleFacade {
  private RoleService roleService;

  public RoleFacade(RoleService roleService) {
    this.roleService = roleService;
  }

  /**
   * Get by ID
   */
  public RoleData get(Long id) {
    final RoleEntity roleEntity = roleService.find(id);
    final RoleData roleData = convert(roleEntity);
    return roleData;
  }

  /**
   * Find pageable data
   */
  public Page<RoleData> find(int page, int size) {
    final PageRequest pageRequest = PageRequest.of(page, size);
    final Page<RoleEntity> entityPage = roleService.find(pageRequest);
    final List<RoleData> dataList = entityPage.getContent().stream().map(entity -> convert(entity)).collect(Collectors.toList());
    final PageImpl<RoleData> dataPage = new PageImpl<RoleData>(dataList, entityPage.getPageable(), entityPage.getTotalElements());
    return dataPage;
  }

  /**
   * Create Entity and save to database
   */
  public RoleData create(RoleForm roleForm) {
    RoleEntity roleEntity = new RoleEntity();
    convert(roleForm,roleEntity);
    roleEntity = roleService.save(roleEntity);
    return convert(roleEntity);
  }

  /**
   * Update Entity  to database
   */
  public RoleData update(Long id, RoleForm roleForm) {
    RoleEntity roleEntity = roleService.find(id);
    convert(roleForm, roleEntity);
    roleEntity = roleService.update(roleEntity);
    return convert(roleEntity);
  }

  /**
   * Delete by ID
   */
  public void delete(Long id) {
    roleService.delete(id);
  }

  /**
   * Convert form to entity object
   */
  private void convert(RoleForm form, RoleEntity roleEntity) {
    BeanUtils.copyProperties(form, roleEntity);
    // TOverride logic. (Copy properties is not the best solution, but an convenient one, for special logic, add below here )
  }

  /**
   * Convert entity to data object
   */
  public RoleData convert(RoleEntity roleEntity) {
    final RoleData roleData = new RoleData();
    BeanUtils.copyProperties(roleEntity, roleData);
    // TODO Override logic. (Copy properties is not the best solution, but an convenient one, for special logic, add below here )
    return roleData;
  }
}
