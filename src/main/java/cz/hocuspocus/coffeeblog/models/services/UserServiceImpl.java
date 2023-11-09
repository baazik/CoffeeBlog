package cz.hocuspocus.coffeeblog.models.services;

import cz.hocuspocus.coffeeblog.data.entities.UserEntity;
import cz.hocuspocus.coffeeblog.data.repositories.UserRepository;
import cz.hocuspocus.coffeeblog.models.dto.UserDTO;
import cz.hocuspocus.coffeeblog.models.exceptions.DuplicateEmailException;
import cz.hocuspocus.coffeeblog.models.exceptions.PasswordsDoNotEqualException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void create(UserDTO user, boolean isAdmin){

        if(!user.getPassword().equals(user.getConfirmPassword())){
            throw new PasswordsDoNotEqualException();
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setAdmin(isAdmin);

        try {
            userRepository.save(userEntity);
        } catch (DataIntegrityViolationException e){
            throw new DuplicateEmailException();
        }

    }

}
