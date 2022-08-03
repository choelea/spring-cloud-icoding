package tech.icoding.sci.sdk.data.detail;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import tech.icoding.sci.sdk.data.PermissionData;

@Data
public class PermissionDetailData extends PermissionData {
  private static final long serialVersionUID = 832957350460136208l;

  private PermissionData parent;

  private Set<PermissionData> children = new HashSet<>();
}
