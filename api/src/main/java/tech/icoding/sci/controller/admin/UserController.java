package tech.icoding.sci.controller.admin;

import java.lang.Long;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.icoding.sci.facade.admin.UserFacade;
import tech.icoding.sci.sdk.common.PageData;
import tech.icoding.sci.sdk.data.UserData;
import tech.icoding.sci.sdk.form.admin.UserForm;

@RestController
@RequestMapping("/users")
public class UserController {
  private UserFacade userFacade;

  public UserController(UserFacade userFacade) {
    this.userFacade = userFacade;
  }

  /**
   * Get by ID
   */
  @GetMapping("/{id}")
  public UserData get(@PathVariable final Long id) {
    final UserData data = userFacade.get(id);
    return data;
  }

  @GetMapping
  public PageData<UserData> find(@RequestParam(defaultValue = "1") int pageNumber,
      @RequestParam(defaultValue = "10") int pageSize) {
    final Page<UserData> page = userFacade.find(pageNumber-1, pageSize);
    return new PageData<>(page.getTotalElements(), page.getNumber(), page.getSize(), page.getContent());
  }

  /**
   * Create
   */
  @PostMapping
  public UserData create(@RequestBody final UserForm form) {
    return userFacade.create(form);
  }

  /**
   * Create
   */
  @PutMapping("/{id}")
  public UserData update(@PathVariable final Long id, @RequestBody final UserForm form) {
    return userFacade.update(id, form);
  }

  /**
   * Delete by ID
   */
  @DeleteMapping("/{id}")
  public void delete(@PathVariable final Long id) {
    userFacade.delete(id);
  }
}
