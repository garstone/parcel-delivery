package kamenev.delivery.tokens.repository;

import kamenev.delivery.tokens.domain.Tokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokensRepository extends JpaRepository<Tokens, UUID> {

    Optional<Tokens> findByRefreshToken(String refreshToken);

}
