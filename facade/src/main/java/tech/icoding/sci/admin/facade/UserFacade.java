package tech.icoding.sci.admin.facade;

import java.lang.Long;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import tech.icoding.sci.admin.data.UserData;
import tech.icoding.sci.admin.form.UserForm;
import tech.icoding.sci.entity.User;
import tech.icoding.sci.service.UserService;

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
    final User user = userService.find(id);
    final UserData userData = convert(user);
    return userData;
  }

  /**
   * Find pageable data
   */
  public Page<UserData> find(int page, int size) {
    final PageRequest pageRequest = PageRequest.of(page, size);
    final Page<User> entityPage = userService.find(pageRequest);
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
    User user = new User();
    convert(userForm,user);
    user = userService.save(user);
    return convert(user);
  }

  /**
   * Update Entity  to database
   */
  public UserData update(Long id, UserForm userForm) {
    User user = userService.find(id);
    convert(userForm, user);
    user = userService.update(user);
    return convert(user);
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
  private void convert(UserForm userForm, User user) {
    BeanUtils.copyProperties(userForm, user);
    // TODO Override logic. (Copy properties is not the best solution, but an convenient one, for special logic, add below here )
  }

  /**
   * Convert entity to data object
   */
  private UserData convert(User user) {
    final UserData userData = new UserData();
    BeanUtils.copyProperties(user, userData);
    // TODO Override logic. (Copy properties is not the best solution, but an convenient one, for special logic, add below here )
    return userData;
  }
}
