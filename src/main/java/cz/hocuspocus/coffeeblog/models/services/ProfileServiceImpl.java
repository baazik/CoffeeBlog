package cz.hocuspocus.coffeeblog.models.services;

import cz.hocuspocus.coffeeblog.data.entities.ProfileEntity;
import cz.hocuspocus.coffeeblog.data.entities.UserEntity;
import cz.hocuspocus.coffeeblog.data.repositories.ProfileRepository;
import cz.hocuspocus.coffeeblog.data.repositories.UserRepository;
import cz.hocuspocus.coffeeblog.models.dto.ProfileDTO;
import cz.hocuspocus.coffeeblog.models.dto.mappers.ProfileMapper;
import cz.hocuspocus.coffeeblog.models.exceptions.ProfileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService{

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ProfileMapper profileMapper;


    @Override
    public ProfileDTO getById(long profileId) {
        ProfileEntity fetchedProfile = getProfileOrThrow(profileId);

        return profileMapper.toDTO(fetchedProfile);
    }

    private ProfileEntity getProfileOrThrow(long profileId) {
        return profileRepository
                .findById(profileId)
                .orElseThrow(ProfileNotFoundException::new);
    }

    @Override
    public ProfileDTO getLoggedUserProfile(){

        // Get email of logged user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        // Get userEntity via userEmail
        UserEntity userEntity = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        //Profile id is the same as user id, so we get this variable here
        long profileId = userEntity.getUserId();

        // we create ProfileEntity fetchedProfile and fill it with found of profileRepository by id
        ProfileEntity fetchedProfile = getProfileOrThrow(profileId);

        // with a mapper, we create a DTO here with that fetchedProfile
        return profileMapper.toDTO(fetchedProfile);

    }


}
