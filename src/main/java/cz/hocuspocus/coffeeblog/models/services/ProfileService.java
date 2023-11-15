package cz.hocuspocus.coffeeblog.models.services;

import cz.hocuspocus.coffeeblog.models.dto.ArticleDTO;
import cz.hocuspocus.coffeeblog.models.dto.ProfileDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface ProfileService{

    ProfileDTO getById(long profileId);

    ProfileDTO getLoggedUserProfile();

    List<ProfileDTO> getAll();

    // long getLoggedUserId();

    void editProfile(ProfileDTO profile);

}
