package cz.hocuspocus.coffeeblog.models.dto.mappers;

import cz.hocuspocus.coffeeblog.data.entities.ProfileEntity;
import cz.hocuspocus.coffeeblog.models.dto.ProfileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileEntity toEntity(ProfileDTO source);

    ProfileDTO toDTO(ProfileEntity source);

    void updateProfileDTO(ProfileDTO source, @MappingTarget ProfileDTO target);

    void updateProfileEntity(ProfileDTO source, @MappingTarget ProfileEntity target);

}
