package tech.icoding.sci.sdk.data.detail;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import tech.icoding.sci.sdk.data.PermissionData;
import tech.icoding.sci.sdk.data.RoleData;

@Data
public class RoleDetailData extends RoleData {
  private static final long serialVersionUID = 860323693881998094l;

  private Set<PermissionData> permissions = new HashSet<>();
}
