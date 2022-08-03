package tech.icoding.sci.facade.admin;

import java.lang.Long;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import tech.icoding.sci.core.entity.RoleEntity;
import tech.icoding.sci.core.service.RoleService;
import tech.icoding.sci.sdk.data.RoleData;
import tech.icoding.sci.sdk.data.detail.RoleDetailData;
import tech.icoding.sci.sdk.form.admin.RoleForm;

@Component
public class RoleFacade {
  private RoleService roleService;

  private PermissionFacade permissionFacade;

  public RoleFacade(RoleService roleService, PermissionFacade permissionFacade) {
    this.roleService = roleService;
    this.permissionFacade = permissionFacade;
  }

  /**
   * Get by ID
   */
  public RoleDetailData get(Long id) {
    final RoleEntity entity = roleService.find(id);
    final RoleDetailData data = convertToDetail(entity);
    return data;
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
  public RoleDetailData create(RoleForm form) {
    RoleEntity entity = new RoleEntity();
    convert(form, entity);
    entity = roleService.save(entity);
    return convertToDetail(entity);
  }

  /**
   * Update Entity  to database
   */
  public RoleDetailData update(Long id, RoleForm form) {
    RoleEntity entity = roleService.find(id);
    convert(form, entity);
    entity = roleService.update(entity);
    return convertToDetail(entity);
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
  public void convert(RoleForm form, RoleEntity entity) {
    BeanUtils.copyProperties(form, entity);
    // TODO Override logic. (Copy properties is not the best solution, but an convenient one, for special logic, add below here )
  }

  /**
   * Convert entity to data object
   */
  public RoleData convert(RoleEntity entity) {
    final RoleData data = new RoleData();
    BeanUtils.copyProperties(entity, data);
    // TODO Override logic. (Copy properties is not the best solution, but an convenient one, for special logic, add below here )
    return data;
  }

  /**
   * Convert entity to data object
   */
  public RoleDetailData convertToDetail(RoleEntity entity) {
    final RoleData data = convert(entity);
    final RoleDetailData detailData = new RoleDetailData();
    BeanUtils.copyProperties(data, detailData);
    entity.getPermissions().forEach( e -> {
                       detailData.getPermissions().add(permissionFacade.convert(e));
                    });
    // TODO Override logic. (Copy properties is not the best solution, but an convenient one, for special logic, add below here )
    return detailData;
  }
}
