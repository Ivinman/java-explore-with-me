package ru.practicum.storage.category;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.category.Category;

public interface CategoriesRepository extends JpaRepository<Category, Integer> {
    Category findByName(String name);
}
