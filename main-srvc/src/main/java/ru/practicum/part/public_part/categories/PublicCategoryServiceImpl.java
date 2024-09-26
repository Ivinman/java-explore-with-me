package ru.practicum.part.public_part.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.category.Category;
import ru.practicum.storage.category.CategoriesRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicCategoryServiceImpl implements  PublicCategoryService {
    private final CategoriesRepository categoriesRepository;

    @Override
    public List<Category> getCategories(Integer from, Integer size) throws Exception {
        try {
            return categoriesRepository.findAll().stream().skip(from).limit(size).toList();
        } catch (Exception e) {
            throw new BadRequestException("Fields filled incorrectly");
        }
    }

    @Override
    public Category getCategory(Integer catId) throws Exception {
        if (categoriesRepository.findById(catId).isPresent()) {
            return categoriesRepository.findById(catId).get();
        }
        throw new NotFoundException("Category with id=" + catId + " was not found");
    }
}
