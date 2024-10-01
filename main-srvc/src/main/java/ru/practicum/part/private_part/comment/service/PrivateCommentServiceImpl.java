package ru.practicum.part.private_part.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentMapper;
import ru.practicum.dto.comment.CommentOutDto;
import ru.practicum.enums.EventState;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.model.comment.Comment;
import ru.practicum.storage.comment.CommentRepository;
import ru.practicum.storage.event.EventRepository;
import ru.practicum.storage.user.UserRepository;

@Service
@RequiredArgsConstructor
public class PrivateCommentServiceImpl implements PrivateCommentService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;

    @Override
    public CommentOutDto addComment(Integer userId, Integer eventId, CommentDto commentDto) throws Exception {
        userAndEventCheck(userId, eventId);
        Comment comment = CommentMapper.toComment(commentDto);
        comment.setUser(userRepository.findById(userId).get());
        comment.setEvent(eventRepository.findById(eventId).get());
        commentRepository.save(comment);
        comment.setId(commentRepository.findFirstByOrderByIdDesc().getId());

        return CommentMapper.toCommOutDto(commentRepository.findFirstByOrderByIdDesc());
    }

    @Override
    public CommentOutDto editComment(Integer userId, Integer eventId, Integer commId, CommentDto commentDto) throws Exception {
        userAndEventCheck(userId, eventId);
        commentAndAuthorCheck(userId, commId);

        Comment comment = commentRepository.findById(commId).get();
        comment.setComment(commentDto.getText());
        commentRepository.save(comment);

        return CommentMapper.toCommOutDto(commentRepository.findById(commId).get());
    }

    @Override
    public void deleteComment(Integer userId, Integer eventId, Integer commId) throws Exception {
        userAndEventCheck(userId, eventId);
        commentAndAuthorCheck(userId, commId);

        commentRepository.deleteById(commId);
    }


    private void commentAndAuthorCheck(Integer userId, Integer commId) throws Exception {
        if (commentRepository.findById(commId).isEmpty()) {
            throw new NotFoundException("Comment with id= " + userId + " not found");
        }
        if (!commentRepository.findById(commId).get().getUser().getId().equals(userId)) {
            throw new ValidationException("User with id= " + userId + " is not the author");
        }
    }

    private void userAndEventCheck(Integer userId, Integer eventId) throws Exception {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("User with id= " + userId + " not found");
        }
        if (eventRepository.findById(eventId).isEmpty()) {
            throw new NotFoundException("Event with id= " + eventId + " not found");
        }
        if (!eventRepository.findById(eventId).get().getState().equals(EventState.PUBLISHED.name())) {
            throw new ForbiddenException("Event must be published");
        }
    }

}
