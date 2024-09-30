package ru.practicum.storage.event;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.event.Event;

import java.sql.Timestamp;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByInitiatorId(Integer userId);

    List<Event> findByCompilationId(Integer compId);

    List<Event> findByCategoryId(Integer catId);

    @Query("select distinct(e.initiator.id) from Event e")
    List<Integer> getAllUserIds();

    @Query("select distinct(e.state) from Event e")
    List<String> getAllStates();

    @Query("select distinct(e.category.id) from Event e")
    List<Integer> getAllCategories();

    @Query("select e from Event e " +
            "where e.initiator.id in (?1) " +
            "and e.state in (?2) " +
            "and e.category.id in (?3) " +
            "and e.eventDate > (?4) " +
            "and e.eventDate < (?5)")
    List<Event> getEvents(List<Integer> userIds, List<String> states, List<Integer> categoryIds,
                          Timestamp start, Timestamp end);

    @Query("select e from Event e " +
            "where e.category.id in (?1) " +
            "and e.paid = ?2 " +
            "and e.eventDate > ?3 " +
            "and e.eventDate < ?4 " +
            "and e.state = ?5 " +
            "and upper(e.annotation) like upper(concat('%', ?6, '%')) " +
            "or upper(e.description) like upper(concat('%', ?6, '%'))")
    List<Event> getPublicEventsWithText(List<Integer> categoryIds, Boolean paid, Timestamp start, Timestamp end,
                                String state, String text, Sort sort);
}
