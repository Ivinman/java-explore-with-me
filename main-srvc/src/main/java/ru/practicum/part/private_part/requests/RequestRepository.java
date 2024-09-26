package ru.practicum.part.private_part.requests;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findByEventId(Integer eventId);
    Request findByEventIdAndRequesterId(Integer eventId, Integer userId);
    List<Request> findByRequesterId(Integer userId);
}
