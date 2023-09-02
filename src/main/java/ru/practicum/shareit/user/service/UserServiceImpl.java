package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper mapper;
    private final UserStorage storage;

    @Override
    public User create(UserDto userDto) {
        return storage.create(mapper.toUser(userDto));
    }

    @Override
    public User update(UserDto userDto, Long userId) {
        User user = mapper.toUser(userDto);
        user.setId(userId);
        return storage.update(user);
    }

    @Override
    public User getById(Long userId) {
        return storage.getById(userId);
    }

    @Override
    public List<User> getAll() {
        return storage.getAll();
    }

    @Override
    public void deleteById(Long userId) {
        storage.deleteById(userId);
    }
}
