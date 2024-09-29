package ru.practicum.part.admin_part.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CategoryMapper;
import ru.practicum.model.category.Category;
import ru.practicum.storage.category.CategoriesRepository;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.storage.event.EventRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminCategoriesServiceImpl implements AdminCategoriesService {
    private final CategoriesRepository categoriesRepository;
    private final EventRepository eventRepository;

    @Override
    public Category addCategory(CategoryDto categoryDto) throws Exception {
        if (categoryDto.getName() == null
                || categoryDto.getName().isBlank()
                || categoryDto.getName().isEmpty()) {
            throw new BadRequestException("Field name filled incorrectly");
        }
        categoryExistValidation(categoryDto);
        categoryNameValid(categoryDto);
        return categoriesRepository.save(CategoryMapper.toCategory(categoryDto));
    }

    @Override
    public void deleteCategory(Integer categoryId) throws Exception {
        if (!eventRepository.findByCategoryId(categoryId).isEmpty()) {
            throw new ConflictException("For the requested operation the conditions are not met.",
                    "The category is not empty");
        }
        if (categoriesRepository.findById(categoryId).isEmpty()) {
            throw new NotFoundException("Category with id=" + categoryId + " was not found");
        }
        categoriesRepository.deleteById(categoryId);
    }

    @Override
    public Category editCategory(Integer categoryId, CategoryDto categoryDto) throws Exception {
        Optional<Category> categoryFromDb = categoriesRepository.findById(categoryId);
        categoryFoundValidation(categoryId, categoryFromDb);
        if (!categoryFromDb.get().getName().equals(categoryDto.getName())) {
            categoryExistValidation(categoryDto);
        }
        categoryNameValid(categoryDto);

        Category category = categoryFromDb.get();
        category.setName(categoryDto.getName());
        return categoriesRepository.save(category);
    }

    private void categoryExistValidation(CategoryDto categoryDto) throws Exception {
        if (categoriesRepository.findByName(categoryDto.getName()) != null) {
            throw new ConflictException("Integrity constraint has been violated.",
                    "Category already exist");
        }
    }

    private void categoryFoundValidation(Integer catId, Optional<Category> category) throws Exception {
        if (category.isEmpty()) {
            throw new NotFoundException("Category with id=" + catId + " was not found");
        }
    }

    private void categoryNameValid(CategoryDto categoryDto) throws Exception {
        if (categoryDto.getName().length() > 50) {
            throw new BadRequestException("Name is too long");
        }
    }
}
