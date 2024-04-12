package cz.hocuspocus.coffeeblog.models.dto.mappers;

import cz.hocuspocus.coffeeblog.data.entities.ProfileEntity;
import cz.hocuspocus.coffeeblog.models.dto.ProfileDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-11T15:38:00+0200",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.10 (OpenLogic)"
)
@Component
public class ProfileMapperImpl implements ProfileMapper {

    @Override
    public ProfileEntity toEntity(ProfileDTO source) {
        if ( source == null ) {
            return null;
        }

        ProfileEntity profileEntity = new ProfileEntity();

        profileEntity.setId( source.getId() );
        profileEntity.setFirstName( source.getFirstName() );
        profileEntity.setLastName( source.getLastName() );
        profileEntity.setBirthday( source.getBirthday() );
        profileEntity.setInterests( source.getInterests() );
        profileEntity.setAboutMe( source.getAboutMe() );
        profileEntity.setNickName( source.getNickName() );

        return profileEntity;
    }

    @Override
    public ProfileDTO toDTO(ProfileEntity source) {
        if ( source == null ) {
            return null;
        }

        ProfileDTO profileDTO = new ProfileDTO();

        profileDTO.setFirstName( source.getFirstName() );
        profileDTO.setLastName( source.getLastName() );
        profileDTO.setBirthday( source.getBirthday() );
        profileDTO.setInterests( source.getInterests() );
        profileDTO.setAboutMe( source.getAboutMe() );
        profileDTO.setId( source.getId() );
        profileDTO.setNickName( source.getNickName() );

        return profileDTO;
    }

    @Override
    public void updateProfileDTO(ProfileDTO source, ProfileDTO target) {
        if ( source == null ) {
            return;
        }

        target.setFirstName( source.getFirstName() );
        target.setLastName( source.getLastName() );
        target.setBirthday( source.getBirthday() );
        target.setInterests( source.getInterests() );
        target.setAboutMe( source.getAboutMe() );
        target.setId( source.getId() );
        target.setAge( source.getAge() );
        target.setNickName( source.getNickName() );
    }

    @Override
    public void updateProfileEntity(ProfileDTO source, ProfileEntity target) {
        if ( source == null ) {
            return;
        }

        target.setId( source.getId() );
        target.setFirstName( source.getFirstName() );
        target.setLastName( source.getLastName() );
        target.setBirthday( source.getBirthday() );
        target.setInterests( source.getInterests() );
        target.setAboutMe( source.getAboutMe() );
        target.setNickName( source.getNickName() );
    }
}
