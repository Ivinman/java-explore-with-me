package ru.practicum.part.public_part.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.category.Category;
import ru.practicum.storage.category.CategoriesRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicCategoryServiceImpl implements  PublicCategoryService {
    private final CategoriesRepository categoriesRepository;

    @Override
    public List<Category> getCategories(Integer from, Integer size) {
        return categoriesRepository.findAll().stream().skip(from).limit(size).toList();
    }

    @Override
    public Category getCategory(Integer catId) throws Exception {
        if (categoriesRepository.findById(catId).isPresent()) {
            return categoriesRepository.findById(catId).get();
        }
        throw new NotFoundException("Category with id=" + catId + " was not found");
    }
}
