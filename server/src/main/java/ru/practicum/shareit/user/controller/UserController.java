package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public User create(@RequestBody UserDto userDto) {
        log.info("Received a POST request: adding a user.");
        return userService.create(userDto);
    }

    @PatchMapping("/{userId}")
    public User update(@RequestBody UserDto userDto, @PathVariable Long userId) {
        log.info("Received a PATCH request: user update.");
        return userService.update(userDto, userId);
    }

    @GetMapping("/{userId}")
    public User getById(@PathVariable Long userId) {
        log.info("A GET request was received: getting a user.");
        return userService.getById(userId);
    }

    @GetMapping
    public List<User> getAll() {
        log.info("A GET request was received: getting all users.");
        return userService.getAll();
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable Long userId) {
        log.info("A DELETE request was received: deleting a user.");
        userService.deleteById(userId);
    }
}
