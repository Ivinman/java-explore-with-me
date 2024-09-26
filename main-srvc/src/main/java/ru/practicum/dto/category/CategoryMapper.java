package ru.practicum.dto.category;

import ru.practicum.model.category.Category;

public class CategoryMapper {
    public static Category toCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        return category;
    }
}
