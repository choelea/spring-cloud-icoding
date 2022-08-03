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
import tech.icoding.sci.facade.admin.PermissionFacade;
import tech.icoding.sci.sdk.common.PageData;
import tech.icoding.sci.sdk.data.PermissionData;
import tech.icoding.sci.sdk.form.admin.PermissionForm;

@RestController
@RequestMapping("/permissions")
public class PermissionController {
  private PermissionFacade permissionFacade;

  public PermissionController(PermissionFacade permissionFacade) {
    this.permissionFacade = permissionFacade;
  }

  /**
   * Get by ID
   */
  @GetMapping("/{id}")
  public PermissionData get(@PathVariable final Long id) {
    final PermissionData data = permissionFacade.get(id);
    return data;
  }

  @GetMapping
  public PageData<PermissionData> find(@RequestParam(defaultValue = "1") int pageNumber,
      @RequestParam(defaultValue = "10") int pageSize) {
    final Page<PermissionData> page = permissionFacade.find(pageNumber-1, pageSize);
    return new PageData<>(page.getTotalElements(), page.getNumber(), page.getSize(), page.getContent());
  }

  /**
   * Create
   */
  @PostMapping
  public PermissionData create(@RequestBody final PermissionForm form) {
    return permissionFacade.create(form);
  }

  /**
   * Create
   */
  @PutMapping("/{id}")
  public PermissionData update(@PathVariable final Long id,
      @RequestBody final PermissionForm form) {
    return permissionFacade.update(id, form);
  }

  /**
   * Delete by ID
   */
  @DeleteMapping("/{id}")
  public void delete(@PathVariable final Long id) {
    permissionFacade.delete(id);
  }
}
