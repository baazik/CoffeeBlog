package cz.hocuspocus.coffeeblog.models.services;

import cz.hocuspocus.coffeeblog.data.entities.ProfileEntity;
import cz.hocuspocus.coffeeblog.data.entities.UserEntity;
import cz.hocuspocus.coffeeblog.data.repositories.ProfileRepository;
import cz.hocuspocus.coffeeblog.data.repositories.UserRepository;
import cz.hocuspocus.coffeeblog.models.dto.ProfileDTO;
import cz.hocuspocus.coffeeblog.models.dto.UserDTO;
import cz.hocuspocus.coffeeblog.models.dto.mappers.ProfileMapper;
import cz.hocuspocus.coffeeblog.models.dto.mappers.UserMapper;
import cz.hocuspocus.coffeeblog.models.exceptions.ProfileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class ProfileServiceImpl implements ProfileService{

    private static final Logger logger = LoggerFactory.getLogger(ProfileServiceImpl.class);

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ProfileMapper profileMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;


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
    public List<ProfileDTO> getAll() {
        return StreamSupport.stream(profileRepository.findAll().spliterator(), false)
                .map(i -> profileMapper.toDTO(i))
                .toList();
    }


    @Override
    public ProfileDTO getLoggedUserProfile(){

        // Get email of logged user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        // Get userEntity via userEmail
        UserEntity userEntity = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        ProfileEntity profileEntity = userEntity.getProfile();
        if (profileEntity == null) {
            logger.error("Profile not found for user: " + userEntity.getEmail());
            throw new ProfileNotFoundException();
        }

        // with a mapper, we create a DTO here with that fetchedProfile
        return profileMapper.toDTO(profileEntity);
    }

    @Override
    public void editProfile(ProfileDTO profile, UserDTO user) {
        ProfileEntity fetchedProfile = getProfileOrThrow(profile.getId());
        UserEntity fetchedUser = fetchedProfile.getUser();
        // we update profile as well as user, because with that, we make sure nickname will be change in both
        profileMapper.updateProfileEntity(profile, fetchedProfile);
        userMapper.updateUserEntity(user, fetchedUser);
        profileRepository.save(fetchedProfile);
        userRepository.save(fetchedUser);
        logger.info("Profile of the user " + fetchedUser.getEmail() + " has been updated.");

    }

    // Metoda pro ukládání obrázku do Base64
    private String saveProfileImage(String profileImage) throws IOException {
        byte[] imageBytes = profileImage.getBytes();
        return Base64.getEncoder().encodeToString(imageBytes);
    }


}
