package cz.hocuspocus.coffeeblog.models.dto.mappers;

import cz.hocuspocus.coffeeblog.data.entities.UserEntity;
import cz.hocuspocus.coffeeblog.models.dto.UserDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-11T15:38:00+0200",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.10 (OpenLogic)"
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
        userEntity.setNickName( source.getNickName() );

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
        userDTO.setNickName( source.getNickName() );

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
        target.setNickName( source.getNickName() );
    }

    @Override
    public void updateUserEntity(UserDTO source, UserEntity target) {
        if ( source == null ) {
            return;
        }

        target.setEmail( source.getEmail() );
        target.setPassword( source.getPassword() );
        target.setNickName( source.getNickName() );
    }
}
