package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User create(UserDto userDto);

    User update(UserDto userDto, Long userId);

    User getById(Long userId);

    List<User> getAll();

    void deleteById(Long userId);
}
