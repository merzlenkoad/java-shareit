package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.handler.exception.NotFoundException;
import ru.practicum.shareit.handler.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserStorageImpl implements UserStorage {
    private Map<Long, User> users;
    private Long id;

    public UserStorageImpl() {
        this.users = new HashMap<>();
        this.id = 0L;
    }

    @Override
    public User create(User user) {
        emailVerification(user);
        user.setId(++id);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        userIdIsExist(user.getId());

        if (user.getName() != null && user.getEmail() != null) {
            emailVerification(user);
            users.replace(user.getId(), user);
        } else if (user.getName() != null && user.getEmail() == null) {
            users.get(user.getId()).setName(user.getName());
        } else if (user.getName() == null && user.getEmail() != null) {
            if (users.get(user.getId()).getEmail().equals(user.getEmail())) {
                users.get(user.getId()).setEmail(user.getEmail());
            } else {
                emailVerification(user);
                users.get(user.getId()).setEmail(user.getEmail());
            }
        }
        return users.get(user.getId());
    }

    @Override
    public User getById(Long userId) {
        userIdIsExist(userId);
        return users.get(userId);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteById(Long userId) {
        userIdIsExist(userId);
        users.remove(userId);
    }

    @Override
    public void userIdIsExist(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("User not found. id=", id);
        }
    }

    @Override
    public void emailVerification(User user) {
        if (users.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new ValidationException("A user with this email already exists.");
        }
    }

}
