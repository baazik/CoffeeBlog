package cz.hocuspocus.coffeeblog.models.services;

import cz.hocuspocus.coffeeblog.models.dto.UserDTO;

public interface UserService {

    void create(UserDTO user, boolean isAdmin);

}
