package se.ki.education.nkcx.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.Collection;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticatedUser extends User {

    private final Long id;
    private final String role;
    private final LocalDateTime lastPasswordResetDate;

    public AuthenticatedUser(String username, String password, boolean enabled,
                             boolean accountNonExpired, boolean credentialsNonExpired,
                             boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, Long id, String role,
                             LocalDateTime lastPasswordResetDate) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.role = role;
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public LocalDateTime getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }
}
