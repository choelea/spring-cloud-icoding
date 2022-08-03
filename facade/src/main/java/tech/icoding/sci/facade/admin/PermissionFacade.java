package tech.icoding.sci.facade.admin;

import java.lang.Long;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import tech.icoding.sci.core.entity.PermissionEntity;
import tech.icoding.sci.core.service.PermissionService;
import tech.icoding.sci.sdk.data.PermissionData;
import tech.icoding.sci.sdk.form.admin.PermissionForm;

@Component
public class PermissionFacade {
  private PermissionService permissionService;

  public PermissionFacade(PermissionService permissionService) {
    this.permissionService = permissionService;
  }

  /**
   * Get by ID
   */
  public PermissionData get(Long id) {
    final PermissionEntity entity = permissionService.find(id);
    final PermissionData data = convert(entity);
    return data;
  }

  /**
   * Find pageable data
   */
  public Page<PermissionData> find(int page, int size) {
    final PageRequest pageRequest = PageRequest.of(page, size);
    final Page<PermissionEntity> entityPage = permissionService.find(pageRequest);
    final List<PermissionData> dataList = entityPage.getContent().stream().map(entity -> convert(entity)).collect(Collectors.toList());
    final PageImpl<PermissionData> dataPage = new PageImpl<PermissionData>(dataList, entityPage.getPageable(), entityPage.getTotalElements());
    return dataPage;
  }

  /**
   * Create Entity and save to database
   */
  public PermissionData create(PermissionForm form) {
    PermissionEntity entity = new PermissionEntity();
    convert(form, entity);
    entity = permissionService.save(entity);
    return convert(entity);
  }

  /**
   * Update Entity  to database
   */
  public PermissionData update(Long id, PermissionForm form) {
    PermissionEntity entity = permissionService.find(id);
    convert(form, entity);
    entity = permissionService.update(entity);
    return convert(entity);
  }

  /**
   * Delete by ID
   */
  public void delete(Long id) {
    permissionService.delete(id);
  }

  /**
   * Convert form to entity object
   */
  public void convert(PermissionForm form, PermissionEntity entity) {
    BeanUtils.copyProperties(form, entity);
    entity.setParent(permissionService.find(form.getParent()));
    // TODO Override logic. (Copy properties is not the best solution, but an convenient one, for special logic, add below here )
  }

  /**
   * Convert entity to data object
   */
  public PermissionData convert(PermissionEntity entity) {
    final PermissionData data = new PermissionData();
    BeanUtils.copyProperties(entity, data);
    // TODO Override logic. (Copy properties is not the best solution, but an convenient one, for special logic, add below here )
    return data;
  }
}
