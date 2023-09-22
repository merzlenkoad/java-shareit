package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Component
public class UserMapper {

    public User toUser(UserDto userDto) {
        return new User(
                userDto.getName(),
                userDto.getEmail()
        );
    }
}
