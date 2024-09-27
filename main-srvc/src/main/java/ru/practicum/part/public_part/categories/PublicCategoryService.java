package ru.practicum.part.public_part.categories;

import ru.practicum.model.category.Category;

import java.util.List;

public interface PublicCategoryService {
    List<Category> getCategories(Integer from, Integer size) throws Exception;

    Category getCategory(Integer catId) throws Exception;
}
