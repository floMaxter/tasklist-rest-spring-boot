package com.projects.tasklist.web.mappers;

import com.projects.tasklist.domain.user.User;
import com.projects.tasklist.web.dto.user.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDto> {
}
