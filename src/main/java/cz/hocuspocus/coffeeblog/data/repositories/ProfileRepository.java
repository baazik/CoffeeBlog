package cz.hocuspocus.coffeeblog.data.repositories;

import cz.hocuspocus.coffeeblog.data.entities.ProfileEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository <ProfileEntity, Long> {

}
