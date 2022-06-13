package kamenev.delivery.identityservice.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kamenev.delivery.identityservice.domain.User;
import kamenev.delivery.identityservice.exceptions.exception.UserRoleDoesNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.*;

@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final String email;
    private final String phoneNumber;
    @JsonIgnore
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl build(User user) {
        Set<GrantedAuthority> auths = new HashSet<>();
        user.getRoles().forEach(role ->
        {
            auths.add(new SimpleGrantedAuthority(role.getName().toString()));
            role.getPermissions().forEach(p -> auths.add(new SimpleGrantedAuthority(p.getName())));
        });

        if (auths.isEmpty()) {
            throw new UserRoleDoesNotExistException();
        }

        return new UserDetailsImpl(user.getId(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getPassword(),
                auths);
    }

    public UUID getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDetailsImpl that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
