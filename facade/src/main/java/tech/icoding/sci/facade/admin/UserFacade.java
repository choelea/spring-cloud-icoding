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
import tech.icoding.sci.core.entity.UserEntity;
import tech.icoding.sci.core.service.RoleService;
import tech.icoding.sci.core.service.UserService;
import tech.icoding.sci.sdk.data.UserData;
import tech.icoding.sci.sdk.form.UserForm;

@Component
public class UserFacade {
  private UserService userService;

  private RoleService roleService;

  public UserFacade(UserService userService, RoleService roleService) {
    this.userService = userService;
    this.roleService = roleService;
  }

  /**
   * Get by ID
   */
  public UserData get(Long id) {
    final UserEntity entity = userService.find(id);
    final UserData data = convert(entity);
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
  public UserData create(UserForm form) {
    UserEntity entity = new UserEntity();
    convert(form, entity);
    entity = userService.save(entity);
    return convert(entity);
  }

  /**
   * Update Entity  to database
   */
  public UserData update(Long id, UserForm form) {
    UserEntity entity = userService.find(id);
    convert(form, entity);
    entity = userService.update(entity);
    return convert(entity);
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
  private void convert(UserForm form, UserEntity entity) {
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
}
