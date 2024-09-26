package ru.practicum.model.event;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.category.Category;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.user.User;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "events")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Event {
    @Column(name = "annotation")
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "description")
    private String description;

    @Column(name = "event_date")
    private Timestamp eventDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User initiator;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "published_on")
    private Timestamp publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Column(name = "state")
    private String state;

    @Column(name = "title")
    private String title;

    @Column(name = "views")
    private Integer views;

    //
    @ManyToOne
    @JoinColumn(name = "compilation_id")
    //@JoinTable(name = "compilation_id", joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "compilation_id", referencedColumnName = "id"))
    private Compilation compilation;
}
