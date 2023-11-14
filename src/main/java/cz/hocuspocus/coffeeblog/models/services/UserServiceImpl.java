package cz.hocuspocus.coffeeblog.models.services;

import cz.hocuspocus.coffeeblog.data.entities.ProfileEntity;
import cz.hocuspocus.coffeeblog.data.entities.UserEntity;
import cz.hocuspocus.coffeeblog.data.repositories.ProfileRepository;
import cz.hocuspocus.coffeeblog.data.repositories.UserRepository;
import cz.hocuspocus.coffeeblog.models.dto.LoggedUserDTO;
import cz.hocuspocus.coffeeblog.models.dto.UserDTO;
import cz.hocuspocus.coffeeblog.models.exceptions.DuplicateEmailException;
import cz.hocuspocus.coffeeblog.models.exceptions.InvalidPasswordException;
import cz.hocuspocus.coffeeblog.models.exceptions.PasswordsDoNotEqualException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

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
        profileEntity.setUser(userEntity);

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username, " + username + " not found"));
    }

}
