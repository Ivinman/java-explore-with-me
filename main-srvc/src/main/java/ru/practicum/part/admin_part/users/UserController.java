package ru.practicum.part.admin_part.users;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.user.UserDto;
import ru.practicum.part.admin_part.users.service.UserService;
import ru.practicum.model.user.User;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User addUser(@RequestBody UserDto userDto) throws Exception {
        return userService.addUser(userDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Integer userId) throws Exception {
        userService.deleteUser(userId);
    }

    @GetMapping
    public List<User> getUsers(@RequestParam(required = false) List<Integer> ids,
                               @RequestParam(defaultValue = "0") Integer from,
                               @RequestParam(defaultValue = "10") Integer size) throws Exception {
        return userService.getUsers(ids, from, size);
    }
}
