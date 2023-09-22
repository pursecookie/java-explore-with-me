package ru.practicum.explore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.models.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}