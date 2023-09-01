package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.CreateGroup;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@Slf4j
@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping
    public User create(@RequestBody @Validated(CreateGroup.class) UserDto userDto) {
        log.info("Получен POST-запрос: добавление пользователя.");
        return userService.create(userDto);
    }

    @PatchMapping("/{userId}")
    public User update(@RequestBody @Valid  UserDto userDto, @PathVariable Long userId) {
        log.info("Получен PATCH-запрос: обновление пользователя.");
        return userService.update(userDto, userId);
    }

    @GetMapping("/{userId}")
    public User getById(@PathVariable Long userId) {
        log.info("Получен GET-запрос: получение пользователя.");
        return userService.getById(userId);
    }

    @GetMapping
    public List<User> getAll() {
        log.info("Получен GET-запрос: получение всех пользователей.");
        return userService.getAll();
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable Long userId) {
        log.info("Получен DELETE-запрос: удаление пользователя.");
        userService.deleteById(userId);
    }
}
