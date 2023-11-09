package cz.hocuspocus.coffeeblog.data.repositories;

import cz.hocuspocus.coffeeblog.data.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

}
