package ru.practicum.explore.services.public_api;

import org.springframework.data.domain.Pageable;
import ru.practicum.explore.dto.category.CategoryDto;

import java.util.Collection;

public interface PublicCategoryService {
    Collection<CategoryDto> readAllCategories(Pageable pageable);

    CategoryDto readEvent(Long catId);
}