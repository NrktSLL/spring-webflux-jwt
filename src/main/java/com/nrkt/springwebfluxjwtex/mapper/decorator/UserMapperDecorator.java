package com.nrkt.springwebfluxjwtex.mapper.decorator;

import com.nrkt.springwebfluxjwtex.model.User;
import com.nrkt.springwebfluxjwtex.mapper.UserMapper;
import com.nrkt.springwebfluxjwtex.payload.request.UserRequestDTO;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public abstract class UserMapperDecorator implements UserMapper {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User userDtoToUser(UserRequestDTO userRequestDTO) {
        var user = userMapper.userDtoToUser(userRequestDTO);
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        if (user.getUsername() == null)
            user.setUsername(userRequestDTO.getName().concat("#").concat(RandomStringUtils.randomAlphanumeric(6)));
        return user;
    }
}
