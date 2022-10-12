package kamenev.delivery.tokens.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tokens")
@Getter
@ToString
@NoArgsConstructor
public class Tokens implements Serializable {
    @Id
    @Column(nullable = false)
    private UUID id;
    @Column(name = "refresh_token", nullable = false)
    String refreshToken;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Date createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    public Tokens(UUID id, String refreshToken) {
        this.id = id;
        this.refreshToken = refreshToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Tokens tokens = (Tokens) o;
        return id != null && Objects.equals(id, tokens.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
