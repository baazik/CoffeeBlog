package cz.hocuspocus.coffeeblog.models.services;

import cz.hocuspocus.coffeeblog.data.entities.PasswordResetTokenEntity;
import cz.hocuspocus.coffeeblog.data.entities.ProfileEntity;
import cz.hocuspocus.coffeeblog.data.entities.UserEntity;
import cz.hocuspocus.coffeeblog.data.repositories.PasswordResetTokenRepository;
import cz.hocuspocus.coffeeblog.data.repositories.ProfileRepository;
import cz.hocuspocus.coffeeblog.data.repositories.UserRepository;
import cz.hocuspocus.coffeeblog.models.dto.LoggedUserDTO;
import cz.hocuspocus.coffeeblog.models.dto.UserDTO;
import cz.hocuspocus.coffeeblog.models.dto.mappers.UserMapper;
import cz.hocuspocus.coffeeblog.models.exceptions.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void create(UserDTO user, boolean isAdmin){

        if(!user.getPassword().equals(user.getConfirmPassword())){
            throw new PasswordsDoNotEqualException();
        }

        UserEntity userEntity = new UserEntity();
        ProfileEntity profileEntity = new ProfileEntity();

        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setAdmin(isAdmin);
        userEntity.setNickName(user.getNickName());
        profileEntity.setUser(userEntity);
        profileEntity.setNickName(user.getNickName());

        try {
            userRepository.save(userEntity);
            profileRepository.save(profileEntity);
        } catch (DataIntegrityViolationException e){
            throw new DuplicateEmailException();
        }

    }

    @Override
    public void changePassword(LoggedUserDTO user) {
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new PasswordsDoNotEqualException();
        }

        // Get email of logged user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        // Get userEntity via userEmail
        UserEntity userEntity = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Check if the current password is correct
        if (!passwordEncoder.matches(user.getCurrentPassword(), userEntity.getPassword())) {
            throw new InvalidPasswordException();
        }

        // Set new password and save to DB
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(userEntity);
    }


    public UserEntity getUserOrThrow(long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public UserDTO getLoggedUserDTO(){

        // Get email of logged user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        // Get userEntity via userEmail
        UserEntity userEntity = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // with a mapper, we create a DTO here with that fetchedProfile
        return userMapper.toDTO(userEntity);
    }

    @Override
    public UserEntity getLoggedUserEntity(){

        // Get email of logged user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        // Get userEntity via userEmail
        UserEntity userEntity = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return userEntity;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username, " + username + " not found"));
    }

    @Override
    public UserEntity findUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Username, " + email + " not found"));
    }

    @Override
    public void createPasswordResetTokenForUser(UserEntity user, String token) {
        PasswordResetTokenEntity myToken = new PasswordResetTokenEntity();
        myToken.setUser(user);
        myToken.setToken(token);
        myToken.setExpiryDate(LocalDateTime.now().plusHours(24));
        passwordResetTokenRepository.save(myToken);
    }

    @Override
    public boolean validatePasswordResetToken(String token) {
        PasswordResetTokenEntity passwordResetToken = passwordResetTokenRepository.findByToken(token)
                .map(result -> (PasswordResetTokenEntity) result)
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));
        return !passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now());
    }

    @Override
    public boolean isValidPasswordResetToken(String token) {
        Optional<PasswordResetTokenEntity> tokenEntity = passwordResetTokenRepository.findByToken(token);

        if (tokenEntity == null) {
            // Token neexistuje v databázi
            return false;
        }

        LocalDateTime expiryDate = tokenEntity.get().getExpiryDate();
        LocalDateTime now = LocalDateTime.now();

        if (expiryDate.isBefore(now)) {
            // Token je expirovaný
            return false;
        }

        // Token je platný
        return true;

    }

    private PasswordResetTokenEntity getTokenOrThrow(long id) {
        return passwordResetTokenRepository
                .findById(id)
                .orElseThrow(ArticleNotFoundException::new);
    }

    @Override
    public void deleteCurrentToken(long tokenId) {
        // Smazání tokenu podle jeho hodnoty
        PasswordResetTokenEntity fetchedToken = getTokenOrThrow(tokenId);
        UserEntity user = fetchedToken.getUser();
        user.setPasswordResetToken(null);
        passwordResetTokenRepository.delete(fetchedToken);
    }

    @Override
    public void resetPassword(String token, String newPassword){
        // Validace tokenu, např. ověření jeho platnosti a existence v databázi
        if (!isValidPasswordResetToken(token)) {
            throw new InvalidTokenException("Invalid or expired token.");
        }

        // Získání uživatele pomocí tokenu
        PasswordResetTokenEntity resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Token not found."));
        UserEntity user = resetToken.getUser();

        // Nastavení nového hesla a uložení do databáze
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Smazání tokenu po úspěšném resetování hesla
        user.setPasswordResetToken(null);
        passwordResetTokenRepository.delete(resetToken);
    }



}
