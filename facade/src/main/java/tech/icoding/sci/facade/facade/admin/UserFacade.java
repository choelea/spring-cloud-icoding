package tech.icoding.sci.facade.facade.admin;

import java.lang.Long;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import tech.icoding.sci.core.entity.UserEntity;
import tech.icoding.sci.core.service.UserService;
import tech.icoding.sci.sdk.data.UserData;
import tech.icoding.sci.sdk.form.admin.UserForm;

@Component
public class UserFacade {
  private UserService userService;

  public UserFacade(UserService userService) {
    this.userService = userService;
  }

  /**
   * Get by ID
   */
  public UserData get(Long id) {
    final UserEntity userEntity = userService.find(id);
    final UserData userData = convert(userEntity);
    return userData;
  }

  /**
   * Find pageable data
   */
  public Page<UserData> find(int page, int size) {
    final PageRequest pageRequest = PageRequest.of(page, size);
    final Page<UserEntity> entityPage = userService.find(pageRequest);
    final List<UserData> dataList = entityPage.getContent().stream().map(entity -> {
                return convert(entity);
            }).collect(Collectors.toList());
    final PageImpl<UserData> dataPage = new PageImpl<UserData>(dataList, entityPage.getPageable(), entityPage.getTotalElements());
    return dataPage;
  }

  /**
   * Create Entity and save to database
   */
  public UserData create(UserForm userForm) {
    UserEntity userEntity = new UserEntity();
    convert(userForm,userEntity);
    userEntity = userService.save(userEntity);
    return convert(userEntity);
  }

  /**
   * Update Entity  to database
   */
  public UserData update(Long id, UserForm userForm) {
    UserEntity userEntity = userService.find(id);
    convert(userForm, userEntity);
    userEntity = userService.update(userEntity);
    return convert(userEntity);
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
  private void convert(UserForm userForm, UserEntity userEntity) {
    BeanUtils.copyProperties(userForm, userEntity);
    // TODO Override logic. (Copy properties is not the best solution, but an convenient one, for special logic, add below here )
  }

  /**
   * Convert entity to data object
   */
  private UserData convert(UserEntity userEntity) {
    final UserData userData = new UserData();
    BeanUtils.copyProperties(userEntity, userData);
    // TODO Override logic. (Copy properties is not the best solution, but an convenient one, for special logic, add below here )
    return userData;
  }
}
