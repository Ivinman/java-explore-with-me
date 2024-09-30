package ru.practicum.storage.event;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.event.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    Location findFirstByOrderByIdDesc();

    Location findByLatAndLon(Double lat, Double lon);
}
