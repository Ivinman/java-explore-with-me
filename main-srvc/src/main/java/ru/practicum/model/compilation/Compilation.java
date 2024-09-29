package ru.practicum.model.compilation;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "compilations")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pinned")
    private Boolean pinned;

    @Column(name = "title")
    private String title;
}
