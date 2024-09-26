package ru.practicum.dto.user;

import ru.practicum.model.user.User;

public class UserMapper {
    public static User toUser(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        return user;
    }

    public static UserWithoutEmailDto toUserWithoutEmailDto (User user) {
        UserWithoutEmailDto userWithoutEmailDto = new UserWithoutEmailDto();
        userWithoutEmailDto.setId(user.getId());
        userWithoutEmailDto.setName(user.getName());
        return userWithoutEmailDto;
    }
}
