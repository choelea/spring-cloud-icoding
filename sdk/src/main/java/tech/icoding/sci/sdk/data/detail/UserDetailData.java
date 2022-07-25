package tech.icoding.sci.sdk.data.detail;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import tech.icoding.sci.sdk.data.RoleData;
import tech.icoding.sci.sdk.data.UserData;

@Data
public class UserDetailData extends UserData {
  private static final long serialVersionUID = 389793338795915787l;

  private RoleData mainRole;

  private Set<RoleData> roles = new HashSet<>();
}
