package ru.practicum.part.admin_part.category.service;

import ru.practicum.model.category.Category;
import ru.practicum.dto.category.CategoryDto;

public interface AdminCategoriesService {
    Category addCategory(CategoryDto categoryDto) throws Exception;
    void deleteCategory(Integer categoryId) throws Exception;
    Category editCategory(Integer categoryId, CategoryDto categoryDto) throws Exception;
}
