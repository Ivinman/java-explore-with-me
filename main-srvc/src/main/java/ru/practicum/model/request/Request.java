package ru.practicum.model.request;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.model.event.Event;
import ru.practicum.model.user.User;

import java.sql.Timestamp;

@Entity
@Table(name = "requests")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Request {
    @Column(name = "created")
    private Timestamp created;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User requester;

    @Column(name = "status")
    private String status;
}
