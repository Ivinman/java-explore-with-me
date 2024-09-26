package ru.practicum.storage.compilation;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.compilation.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Integer> {
    List<Compilation> findByPinned(Boolean pinned);
    Compilation findFirstByOrderByIdDesc();
    Compilation findByTitle (String title);
}