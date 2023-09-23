package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Validated(CreateGroup.class)UserDto userDto) {
        log.info("Received a POST request: adding a user.");
        return userClient.create(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@RequestBody @Valid  UserDto userDto, @PathVariable Long userId) {
        log.info("Received a PATCH request: user update.");
        return userClient.update(userDto, userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@PathVariable Long userId) {
        log.info("A GET request was received: getting a user.");
        return userClient.getById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("A GET request was received: getting all users.");
        return userClient.getAll();
    }

    @DeleteMapping("/{userId}")
    public HttpStatus delete(@PathVariable @Positive Long userId) {
        log.info("A DELETE request was received: deleting a user.");
        userClient.delete(userId);
        return HttpStatus.OK;
    }
}
