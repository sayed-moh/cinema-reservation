package murraco.model;

import org.springframework.security.core.GrantedAuthority;

public enum AppUserRole implements GrantedAuthority {
  ROLE_ADMIN, ROLE_CLIENT,ROLE_MANAGER,ROLE_GUEST;

  public String getAuthority() {
    return name();
  }

}
