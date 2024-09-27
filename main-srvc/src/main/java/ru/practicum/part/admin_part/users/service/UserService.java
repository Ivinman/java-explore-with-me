package ru.practicum.part.admin_part.users.service;

import ru.practicum.dto.user.UserDto;
import ru.practicum.model.user.User;

import java.util.List;

public interface UserService {
    User addUser(UserDto userDto) throws Exception;

    void deleteUser(Integer userId) throws Exception;

    List<User> getUsers(List<Integer> userIds, Integer from, Integer size) throws Exception;
}
