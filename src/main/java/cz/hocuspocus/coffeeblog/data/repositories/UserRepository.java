package cz.hocuspocus.coffeeblog.data.repositories;

import cz.hocuspocus.coffeeblog.data.entities.PasswordResetTokenEntity;
import cz.hocuspocus.coffeeblog.data.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByPasswordResetToken_Token(String token);

    Optional<UserEntity> findByPasswordResetToken(PasswordResetTokenEntity passwordResetToken);

}
