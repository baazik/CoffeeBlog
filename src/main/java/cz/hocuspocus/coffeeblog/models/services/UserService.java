package cz.hocuspocus.coffeeblog.models.services;

import cz.hocuspocus.coffeeblog.data.entities.PasswordResetTokenEntity;
import cz.hocuspocus.coffeeblog.data.entities.UserEntity;
import cz.hocuspocus.coffeeblog.models.dto.LoggedUserDTO;
import cz.hocuspocus.coffeeblog.models.dto.UserDTO;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void create(UserDTO user, boolean isAdmin);

    void changePassword(LoggedUserDTO user);

    UserDTO getLoggedUserDTO();

    UserEntity getLoggedUserEntity();

    UserEntity getUserOrThrow(long userId);

    UserEntity findUserByEmail(String email);

    void createPasswordResetTokenForUser(UserEntity user, String token);

    boolean validatePasswordResetToken(String token);

    void resetPassword(String token, String newPassword);

    boolean isValidPasswordResetToken(String token);

    void deleteCurrentToken(long tokenId);

}
