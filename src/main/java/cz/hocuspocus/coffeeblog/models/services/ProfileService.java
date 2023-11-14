package cz.hocuspocus.coffeeblog.models.services;

import cz.hocuspocus.coffeeblog.models.dto.ProfileDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface ProfileService{

    ProfileDTO getById(long profileId);

    ProfileDTO getLoggedUserProfile();

}
