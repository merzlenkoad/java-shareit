package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.handler.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplUnitTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository repository;

    @Test
    void updateWithNameAndEmailIsNull() {
        User user = new User(1L, "user", "user@user.com");
        UserDto userDto = new UserDto(null, null);

        when(repository.save(any())).thenReturn(user);
        when(repository.findById(anyLong())).thenReturn(Optional.of(user));

        User actualUser = userService.update(userDto, 1L);

        assertEquals(user, actualUser);
    }

    @Test
    void getUserIfExists() {
        assertThrows(NotFoundException.class, () -> {
            userService.getUserIfExists(anyLong());
        });
    }
}