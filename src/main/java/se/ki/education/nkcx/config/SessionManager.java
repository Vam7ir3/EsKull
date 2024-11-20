package se.ki.education.nkcx.config;

//import se.ki.education.nkcx.enums.Authority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import se.ki.education.nkcx.entity.AuthorityEntity;

import java.util.ArrayList;
import java.util.List;

public class SessionManager {
    public static AuthenticatedUser getAuthenticatedUser() {
        return (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static Long getUserId() {
        return getAuthenticatedUser().getId();
    }

    public static boolean isAnonymousUser() {
        return SecurityContextHolder.getContext().getAuthentication() == null || SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser");
    }

    public static List<AuthorityEntity> getAuthorities() {
        List<AuthorityEntity> authorities = new ArrayList<>();
        if (getAuthenticatedUser().getAuthorities() != null) {
            for (GrantedAuthority grantedAuthority : getAuthenticatedUser().getAuthorities()) {
                AuthorityEntity authorityEntity = new AuthorityEntity();
                authorityEntity.setTitle(grantedAuthority.getAuthority());
                authorities.add(authorityEntity);
            }
        }
        return authorities;
    }

    public static String getRole() {
        return getAuthenticatedUser().getRole();
    }

    public static boolean isSuperAdmin() {
        return !isAnonymousUser() && getAuthenticatedUser().getRole().equalsIgnoreCase("Super Admin");
    }

}
