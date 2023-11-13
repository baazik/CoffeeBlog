package cz.hocuspocus.coffeeblog.models.dto.mappers;

import cz.hocuspocus.coffeeblog.data.entities.ArticleEntity;
import cz.hocuspocus.coffeeblog.data.entities.UserEntity;
import cz.hocuspocus.coffeeblog.models.dto.ArticleDTO;
import cz.hocuspocus.coffeeblog.models.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    UserEntity toEntity(UserDTO source);

    UserDTO toDTO(UserEntity source);

    void updateUserDTO(UserDTO source, @MappingTarget UserDTO target);

    void updateUserEntity(UserDTO source, @MappingTarget UserEntity target);

}
