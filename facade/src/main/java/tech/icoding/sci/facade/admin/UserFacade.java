package tech.icoding.sci.facade.admin;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import tech.icoding.sci.core.entity.RoleEntity;
import tech.icoding.sci.core.entity.UserEntity;
import tech.icoding.sci.core.service.RoleService;
import tech.icoding.sci.core.service.UserService;
import tech.icoding.sci.sdk.data.UserData;
import tech.icoding.sci.sdk.data.detail.UserDetailData;
import tech.icoding.sci.sdk.form.admin.UserForm;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserFacade {
  private UserService userService;

  private RoleFacade roleFacade;

  private RoleService roleService;

  public UserFacade(UserService userService, RoleFacade roleFacade, RoleService roleService) {
    this.userService = userService;
    this.roleFacade = roleFacade;
    this.roleService = roleService;
  }

  /**
   * Get by ID
   */
  public UserDetailData get(Long id) {
    final UserEntity entity = userService.find(id);
    final UserDetailData data = convertToDetail(entity);
    return data;
  }

  /**
   * Find pageable data
   */
  public Page<UserData> find(int page, int size) {
    final PageRequest pageRequest = PageRequest.of(page, size);
    final Page<UserEntity> entityPage = userService.find(pageRequest);
    final List<UserData> dataList = entityPage.getContent().stream().map(entity -> convert(entity)).collect(Collectors.toList());
    final PageImpl<UserData> dataPage = new PageImpl<UserData>(dataList, entityPage.getPageable(), entityPage.getTotalElements());
    return dataPage;
  }

  /**
   * Create Entity and save to database
   */
  public UserDetailData create(UserForm form) {
    UserEntity entity = new UserEntity();
    convert(form, entity);
    entity = userService.save(entity);
    return convertToDetail(entity);
  }

  /**
   * Update Entity  to database
   */
  public UserDetailData update(Long id, UserForm form) {
    UserEntity entity = userService.find(id);
    convert(form, entity);
    entity = userService.update(entity);
    return convertToDetail(entity);
  }

  /**
   * Delete by ID
   */
  public void delete(Long id) {
    userService.delete(id);
  }

  /**
   * Convert form to entity object
   */
  public void convert(UserForm form, UserEntity entity) {
    BeanUtils.copyProperties(form, entity);
    entity.setMainRole(roleService.find(form.getMainRole()));
    form.getRoles().forEach(id->{ 
        final RoleEntity ent = roleService.find(id);
        entity.getRoles().add(ent);
        });
    // TODO Override logic. (Copy properties is not the best solution, but an convenient one, for special logic, add below here )
  }

  /**
   * Convert entity to data object
   */
  private UserData convert(UserEntity entity) {
    final UserData data = new UserData();
    BeanUtils.copyProperties(entity, data);
    // TODO Override logic. (Copy properties is not the best solution, but an convenient one, for special logic, add below here )
    return data;
  }

  /**
   * Convert entity to data object
   */
  public UserDetailData convertToDetail(UserEntity entity) {
    final UserData data = convert(entity);
    final UserDetailData detailData = new UserDetailData();
    BeanUtils.copyProperties(data, detailData);
    detailData.setMainRole(roleFacade.convert(entity.getMainRole()));
    entity.getRoles().forEach( e -> {
                       detailData.getRoles().add(roleFacade.convert(e));
                    });
    // TODO Override logic. (Copy properties is not the best solution, but an convenient one, for special logic, add below here )
    return detailData;
  }
}
