package ru.practicum.storage.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.request.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findByEventId(Integer eventId);

    Request findByEventIdAndRequesterId(Integer eventId, Integer userId);

    List<Request> findByRequesterId(Integer userId);
}
