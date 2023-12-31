package cz.hocuspocus.coffeeblog.models.services;

import cz.hocuspocus.coffeeblog.models.dto.ArticleDTO;
import cz.hocuspocus.coffeeblog.models.dto.ProfileDTO;
import cz.hocuspocus.coffeeblog.models.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProfileService{

    ProfileDTO getById(long profileId);

    ProfileDTO getLoggedUserProfile();

    List<ProfileDTO> getAll();

    // long getLoggedUserId();

    void editProfile(ProfileDTO profile, UserDTO user);

}
