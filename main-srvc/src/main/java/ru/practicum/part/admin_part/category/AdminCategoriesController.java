package ru.practicum.part.admin_part.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.model.category.Category;
import ru.practicum.part.admin_part.category.service.AdminCategoriesService;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoriesController {
    private final AdminCategoriesService adminCategoriesService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Category addCategory(@RequestBody CategoryDto categoryDto) throws Exception {
        return adminCategoriesService.addCategory(categoryDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable Integer catId) throws Exception {
        adminCategoriesService.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public Category editCategory(@PathVariable Integer catId,
                             @RequestBody CategoryDto categoryDto) throws Exception {
        return adminCategoriesService.editCategory(catId, categoryDto);
    }
}
