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
import tech.icoding.sci.facade.admin.RoleFacade;
import tech.icoding.sci.sdk.common.PageData;
import tech.icoding.sci.sdk.data.RoleData;
import tech.icoding.sci.sdk.form.admin.RoleForm;

@RestController
@RequestMapping("/roles")
public class RoleController {
  private RoleFacade roleFacade;

  public RoleController(RoleFacade roleFacade) {
    this.roleFacade = roleFacade;
  }

  /**
   * Get by ID
   */
  @GetMapping("/{id}")
  public RoleData get(@PathVariable final Long id) {
    final RoleData data = roleFacade.get(id);
    return data;
  }

  @GetMapping
  public PageData<RoleData> find(@RequestParam(defaultValue = "1") int pageNumber,
      @RequestParam(defaultValue = "10") int pageSize) {
    final Page<RoleData> page = roleFacade.find(pageNumber-1, pageSize);
    return new PageData<>(page.getTotalElements(), page.getNumber(), page.getSize(), page.getContent());
  }

  /**
   * Create
   */
  @PostMapping
  public RoleData create(@RequestBody final RoleForm form) {
    return roleFacade.create(form);
  }

  /**
   * Create
   */
  @PutMapping("/{id}")
  public RoleData update(@PathVariable final Long id, @RequestBody final RoleForm form) {
    return roleFacade.update(id, form);
  }

  /**
   * Delete by ID
   */
  @DeleteMapping("/{id}")
  public void delete(@PathVariable final Long id) {
    roleFacade.delete(id);
  }
}
