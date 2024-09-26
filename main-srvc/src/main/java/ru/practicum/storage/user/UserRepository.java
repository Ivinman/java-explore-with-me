package ru.practicum.storage.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.user.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select u from User u " +
            "where u.id in (?1)")
    List<User> findByUserIds(List<Integer> userIds);

    User findByNameAndEmail(String name, String email);
}
