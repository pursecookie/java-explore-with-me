package ru.practicum.explore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}