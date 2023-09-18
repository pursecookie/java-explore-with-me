package ru.practicum.explore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.models.HitEntity;
import ru.practicum.explore.models.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EndpointHitRepository extends JpaRepository<HitEntity, Long> {
    @Query("SELECT NEW ru.practicum.explore.models.ViewStats(" +
            "hits.app, hits.uri, COUNT(hits.ip)) " +
            "FROM HitEntity hits " +
            "WHERE hits.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY hits.app, hits.uri " +
            "ORDER BY COUNT(hits) DESC")
    List<ViewStats> readAllStats(LocalDateTime start,
                                 LocalDateTime end);

    @Query("SELECT NEW ru.practicum.explore.models.ViewStats(" +
            "hits.app, hits.uri, COUNT(hits.ip)) " +
            "FROM HitEntity hits " +
            "WHERE hits.timestamp BETWEEN ?1 AND ?2 " +
            "AND hits.uri IN ?3 " +
            "GROUP BY hits.app, hits.uri " +
            "ORDER BY COUNT(hits) DESC")
    List<ViewStats> readStatsWithUris(LocalDateTime start,
                                      LocalDateTime end,
                                      List<String> uris);

    @Query("SELECT NEW ru.practicum.explore.models.ViewStats(" +
            "hits.app, hits.uri, COUNT(DISTINCT hits.ip)) " +
            "FROM HitEntity hits " +
            "WHERE hits.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY hits.app, hits.uri " +
            "ORDER BY COUNT(DISTINCT hits) DESC")
    List<ViewStats> readStatsWithUniqueViews(LocalDateTime start,
                                             LocalDateTime end);

    @Query("SELECT NEW ru.practicum.explore.models.ViewStats(" +
            "hits.app, hits.uri, COUNT(DISTINCT hits.ip)) " +
            "FROM HitEntity hits " +
            "WHERE hits.timestamp BETWEEN ?1 AND ?2 " +
            "AND hits.uri IN ?3 " +
            "GROUP BY hits.app, hits.uri " +
            "ORDER BY COUNT(DISTINCT hits) DESC")
    List<ViewStats> readStatsWithUrisAndUniqueViews(LocalDateTime start,
                                                    LocalDateTime end,
                                                    List<String> uris);
}