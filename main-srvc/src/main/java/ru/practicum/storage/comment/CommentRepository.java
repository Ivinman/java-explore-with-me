package ru.practicum.storage.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.comment.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Comment findFirstByOrderByIdDesc();
    
    List<Comment> findByEventId(Integer eventId);
}
