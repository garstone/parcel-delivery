package kamenev.delivery.common.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class UserDetails implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String email;
    private String phoneNumber;
    @Builder.Default
    private Set<? extends GrantedAuthority> authorities = new HashSet<>();
    @JsonIgnore
    private String password;

    public String stringifyAuthorities() {
        return StringUtils.collectionToCommaDelimitedString(authorities);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDetails that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
