package com.nrkt.springwebfluxjwtex.mapper;

import com.nrkt.springwebfluxjwtex.mapper.decorator.UserMapperDecorator;
import com.nrkt.springwebfluxjwtex.model.User;
import com.nrkt.springwebfluxjwtex.payload.request.UserRequestDTO;
import com.nrkt.springwebfluxjwtex.payload.response.UserResponseDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
@DecoratedWith(UserMapperDecorator.class)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "userRole", ignore = true)
    @Mapping(target = "active", ignore = true)
    User userDtoToUser(UserRequestDTO userRequestDTO);

    UserResponseDTO userToUserResponse(User user);
}
