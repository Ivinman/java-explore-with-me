package ru.practicum.part.admin_part.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.storage.user.UserRepository;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserMapper;
import ru.practicum.model.user.User;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User addUser(UserDto userDto) throws Exception {
        if (userRepository.findByNameAndEmail(userDto.getName(), userDto.getEmail()) != null) {
            throw new ConflictException("Integrity constraint has been violated.",
                    "User already exist");
        }
        if(userDto.getName() == null || userDto.getName().isEmpty() || userDto.getName().isBlank()) {
            throw new BadRequestException("Field name filled incorrectly");
        }
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty() || userDto.getEmail().isBlank()) {
            throw new BadRequestException("Field email filled incorrectly");
        }

        if (userDto.getName().length() < 2) {
            throw new BadRequestException("Name is too short");
        }
        if (userDto.getName().length() > 250) {
            throw new BadRequestException("Name is too long");
        }
        if (userDto.getEmail().length() < 6) {
            throw new BadRequestException("Email is too short");
        }
        if (userDto.getEmail().length() > 254) {
            throw new BadRequestException("Email is too long");
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
    public List<User> getUsers(List<Integer> userIds, Integer from, Integer size) throws Exception {
        try {
            List<User> usersFromDb = new ArrayList<>();
            if (userIds == null) {
                usersFromDb = userRepository.findAll();
                return usersFromDb.stream().skip(from).limit(size).toList();
            }
            usersFromDb = userRepository.findByUserIds(userIds);
            return usersFromDb.stream().skip(from).limit(size).toList();
        } catch (Exception e) {
            throw new BadRequestException("Fields filled incorrectly");
        }

    }
}
