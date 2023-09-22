package ru.practicum.explore.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.models.Event;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Collection<Event> findAllByIdIn(List<Long> events);

    @Query("SELECT e FROM Event e " +
            "WHERE e.initiator.id IN ?1")
    Page<Event> readAllInitiatorEvents(Long userId, Pageable pageable);

    Collection<Event> findAllByCategory_Id(Long catId);

    @Query("SELECT e FROM Event e " +
            "WHERE ((:initiators IS NULL) OR (e.initiator.id IN :initiators)) " +
            "AND ((:states IS NULL) OR (e.state IN :states)) " +
            "AND ((:categories IS NULL) OR (e.category.id IN :categories)) " +
            "AND ((CAST(:rangeStart AS date) IS NULL) OR (e.eventDate >= :rangeStart)) " +
            "AND ((CAST(:rangeEnd AS date) IS NULL) OR (e.eventDate <= :rangeEnd)) ")
    Page<Event> findEventsByAdminParameters(List<Long> initiators, List<String> states, List<Long> categories,
                                            LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE ((:text IS NULL) OR (upper(e.annotation) LIKE upper(concat('%', :text, '%')) " +
            "OR upper(e.description) LIKE upper(concat('%', :text, '%')))) " +
            "AND ((:categories IS NULL) OR (e.category.id IN :categories)) " +
            "AND ((:paid IS NULL) OR (e.paid = :paid)) " +
            "AND ((CAST(:rangeStart AS date) IS NULL) OR (e.eventDate >= :rangeStart)) " +
            "AND ((CAST(:rangeEnd AS date) IS NULL) OR (e.eventDate <= :rangeEnd)) " +
            "AND e.state = 'PUBLISHED'")
    Page<Event> findEventsByUserParameters(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd, Pageable pageable);
}