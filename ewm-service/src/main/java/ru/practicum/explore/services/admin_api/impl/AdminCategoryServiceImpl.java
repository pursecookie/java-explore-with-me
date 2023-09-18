package ru.practicum.explore.services.admin_api.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.category.CategoryDto;
import ru.practicum.explore.dto.category.NewCategoryDto;
import ru.practicum.explore.exceptions.ConditionMismatchException;
import ru.practicum.explore.mappers.CategoryMapper;
import ru.practicum.explore.models.Category;
import ru.practicum.explore.models.Event;
import ru.practicum.explore.repositories.CategoryRepository;
import ru.practicum.explore.repositories.EventRepository;
import ru.practicum.explore.services.admin_api.AdminCategoryService;
import ru.practicum.explore.util.DataFinder;
import ru.practicum.explore.util.UtilMergeProperty;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final DataFinder dataFinder;

    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.mapToCategory(newCategoryDto);

        return CategoryMapper.mapToCategoryDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long catId) {
        dataFinder.findCategoryById(catId);

        Collection<Event> eventsByCategory = eventRepository.findAllByCategory_Id(catId);

        if (!eventsByCategory.isEmpty()) {
            throw new ConditionMismatchException("The category is not empty");
        }

        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        Category categoryToUpdate = dataFinder.findCategoryById(catId);

        UtilMergeProperty.copyProperties(categoryDto, categoryToUpdate);

        return CategoryMapper.mapToCategoryDto(categoryRepository.save(categoryToUpdate));
    }
}