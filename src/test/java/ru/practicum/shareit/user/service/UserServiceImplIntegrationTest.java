package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceImplIntegrationTest {

    @Autowired
    private UserService userService;
    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        userDto = new UserDto("user", "user@user.com");
    }

    @Test
    void shouldCreateTest() {
        User user = userService.create(userDto);
        assertNotNull(user.getId());

        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getName(), user.getName());
    }

    @Test
    void shouldUpdateTest() {
        User user = userService.create(userDto);
        assertNotNull(user.getId());

        UserDto updateUser = new UserDto("update", "update@user.com");
        Long userId = 1L;

        User actualUser = userService.update(updateUser, userId);

        assertEquals(updateUser.getName(), actualUser.getName());
        assertEquals(updateUser.getEmail(), actualUser.getEmail());
        assertEquals(userId, actualUser.getId());
    }

    @Test
    void shouldGetByIdTest() {
        User user = userService.create(userDto);
        assertNotNull(user.getId());

        User actualUser = userService.getById(user.getId());
        assertEquals(user, actualUser);
    }

    @Test
    void shouldGetAllTest() {
        UserDto userDto2 = new UserDto("user2", "user2@user.com");

        User user = userService.create(userDto);
        User user2 = userService.create(userDto2);

        List<User> expectedUsers = List.of(user, user2);
        List<User> actualUsers = userService.getAll();

        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void shouldDeleteByIdTest() {
        User user = userService.create(userDto);
        assertNotNull(user.getId());

        userService.deleteById(user.getId());

        assertEquals(new ArrayList<>(), userService.getAll());
    }

    @Test
    void shouldGetUserIfExistsTest() {
        userService.create(userDto);
        User user = userService.getUserIfExists(1L);
        assertNotNull(user.getId());
    }
}