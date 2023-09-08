package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    User create(User user);

    User update(User user);

    User getById(Long userId);

    List<User> getAll();

    void deleteById(Long userId);

    void userIdIsExist(Long id);

    void emailVerification(User user);
}
