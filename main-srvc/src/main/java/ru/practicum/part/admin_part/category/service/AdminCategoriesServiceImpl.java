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
        if (categoriesRepository.findByName(categoryDto.getName()) != null) {
            throw new ConflictException("Integrity constraint has been violated.",
                    "Category already exist");
        }
        if (categoryDto.getName().length() > 50) {
            throw new BadRequestException("Name is too long");
        }
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
        if (categoriesRepository.findById(categoryId).isEmpty()) {
            throw new NotFoundException("Category with id=" + categoryId + " was not found");
        }
        if (!categoriesRepository.findById(categoryId).get().getName().equals(categoryDto.getName())) {
            if (categoriesRepository.findByName(categoryDto.getName()) != null) {
                throw new ConflictException("Integrity constraint has been violated.",
                        "Category already exist");
            }
        }
        if (categoryDto.getName().length() > 50) {
            throw new BadRequestException("Name is too long");
        }

        Category category = categoriesRepository.findById(categoryId).get();
        category.setName(categoryDto.getName());
        return categoriesRepository.save(category);
    }
}
