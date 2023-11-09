package cz.hocuspocus.coffeeblog.models.services;

import cz.hocuspocus.coffeeblog.models.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void create(UserDTO user, boolean isAdmin);

}
