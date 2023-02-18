package dat3.car.security.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import dat3.car.security.entity.UserWithRoles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserWithRolesRequest {
    @JsonProperty
    private String username;

    @JsonProperty
    private String password;

    @JsonProperty
    private boolean isAccountNonExpired = true;

    @JsonProperty
    private boolean isAccountNonLocked = true;

    @JsonProperty
    private boolean isCredentialsNonExpired = true;

    @JsonProperty
    private boolean isEnabled = true;

    @JsonProperty
    private List<String> roles;

    public UserWithRolesRequest(UserWithRoles userWithRoles) {
        username = userWithRoles.getUsername();
        password = userWithRoles.getPassword();
        isAccountNonExpired = userWithRoles.isAccountNonExpired();
        isAccountNonLocked = userWithRoles.isAccountNonLocked();
        isEnabled = userWithRoles.isEnabled();
        isCredentialsNonExpired = userWithRoles.isCredentialsNonExpired();
        roles = userWithRoles.getRoles();
    }

    public UserWithRoles toUserWithRoles() {
        return UserWithRoles.builder()
            .username(username)
            .password(password)
            .isAccountNonExpired(isAccountNonExpired)
            .isAccountNonLocked(isAccountNonLocked)
            .isCredentialsNonExpired(isCredentialsNonExpired)
            .isEnabled(isEnabled)
            .roles(roles)
            .build();
    }
}
