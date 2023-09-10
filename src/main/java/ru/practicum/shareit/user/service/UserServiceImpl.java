package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.handler.exception.NotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper mapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public User create(UserDto userDto) {
            return userRepository.save(mapper.toUser(userDto));
    }

    @Override
    @Transactional
    public User update(UserDto userDto, Long userId) {
        User user = checkUserIsExists(userId);
        if (!Strings.isBlank(userDto.getName())) {
            user.setName(userDto.getName());
        }
        if (!Strings.isBlank(userDto.getEmail())) {
            user.setEmail(userDto.getEmail());
        }
        return userRepository.save(user);
    }

    @Override
    public User getById(Long userId) {
        return checkUserIsExists(userId);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(Long userId) {
        checkUserIsExists(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public User checkUserIsExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found. id = ", userId));
    }
}
