package ru.practicum.explore.services.admin_api;

import ru.practicum.explore.dto.category.CategoryDto;
import ru.practicum.explore.dto.category.NewCategoryDto;

public interface AdminCategoryService {
    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(Long catId);

    CategoryDto updateCategory(Long catId, CategoryDto categoryDto);

}