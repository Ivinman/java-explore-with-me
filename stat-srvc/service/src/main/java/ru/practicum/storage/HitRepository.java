package ru.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Hit;

import java.sql.Timestamp;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Integer> {
    @Query("select h from Hit h " +
            "where h.uri in (?1) " +
            "and h.timestamp between ?2 and ?3")
    List<Hit> getHitByUrisAndTime(List<String> uris, Timestamp start, Timestamp end);

    @Query("select count(h) from Hit h " +
            "where h.uri = ?1 " +
            "and h.timestamp between ?2 and ?3")
    Integer getUriHits(String uri, Timestamp start, Timestamp end);

    @Query("select distinct h.ip from Hit h")
    List<String> getDistinctIp();

    @Query("select distinct h.uri from Hit h")
    List<String> getDistinctUri();
}
