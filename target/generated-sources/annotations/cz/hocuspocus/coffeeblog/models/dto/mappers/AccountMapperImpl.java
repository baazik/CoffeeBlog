package cz.hocuspocus.coffeeblog.models.dto.mappers;

import cz.hocuspocus.coffeeblog.data.entities.UserEntity;
import cz.hocuspocus.coffeeblog.models.dto.UserDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-10T16:37:51+0100",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.7 (Eclipse Adoptium)"
)
@Component
public class AccountMapperImpl implements AccountMapper {

    @Override
    public UserEntity toEntity(UserDTO source) {
        if ( source == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setEmail( source.getEmail() );
        userEntity.setPassword( source.getPassword() );

        return userEntity;
    }

    @Override
    public UserDTO toDTO(UserEntity source) {
        if ( source == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setEmail( source.getEmail() );
        userDTO.setPassword( source.getPassword() );

        return userDTO;
    }

    @Override
    public void updateUserDTO(UserDTO source, UserDTO target) {
        if ( source == null ) {
            return;
        }

        target.setEmail( source.getEmail() );
        target.setPassword( source.getPassword() );
        target.setConfirmPassword( source.getConfirmPassword() );
    }

    @Override
    public void updateUserEntity(UserDTO source, UserEntity target) {
        if ( source == null ) {
            return;
        }

        target.setEmail( source.getEmail() );
        target.setPassword( source.getPassword() );
    }
}
