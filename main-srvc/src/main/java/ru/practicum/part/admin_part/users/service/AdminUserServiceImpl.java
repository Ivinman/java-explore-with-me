package ru.practicum.part.admin_part.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ValidationException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.storage.user.UserRepository;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserMapper;
import ru.practicum.model.user.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository userRepository;

    @Override
    public User addUser(UserDto userDto) throws Exception {
        if (userRepository.findByNameAndEmail(userDto.getName(), userDto.getEmail()) != null) {
            throw new ConflictException("Integrity constraint has been violated.",
                    "User already exist");
        }

        String[] emailSplit = userDto.getEmail().split("@");
        String[] emailDomSplit = emailSplit[1].split("\\.");
        if (emailSplit[0].length() > 64) {
            throw new ValidationException("Email name is too long");
        }
        if (emailDomSplit[0].length() > 63) {
            throw new ValidationException("Email domain is too long");
        }

        return userRepository.save(UserMapper.toUser(userDto));
    }

    @Override
    public void deleteUser(Integer userId) throws Exception {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public List<User> getUsers(List<Integer> userIds, Integer from, Integer size) {
        List<User> usersFromDb;
        if (userIds == null) {
            usersFromDb = userRepository.findAll();
            return usersFromDb.stream().skip(from).limit(size).toList();
        }
        usersFromDb = userRepository.findByUserIds(userIds);
        return usersFromDb.stream().skip(from).limit(size).toList();
    }
}
