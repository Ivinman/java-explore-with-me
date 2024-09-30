package ru.practicum.part.public_part.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.category.Category;
import ru.practicum.part.public_part.categories.service.PublicCategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class PublicCategoryController {
    private final PublicCategoryService categoryService;

    @GetMapping
    public List<Category> getCategories(@RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(defaultValue = "10") Integer size) throws Exception {
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public Category getCategory(@PathVariable Integer catId) throws Exception {
        return categoryService.getCategory(catId);
    }
}
