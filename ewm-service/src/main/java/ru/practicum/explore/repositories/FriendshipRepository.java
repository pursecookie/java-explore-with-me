package ru.practicum.explore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.models.Friendship;

import java.util.Collection;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    Friendship findByUserIdAndFriendId(Long userId, Long friendId);

    Collection<Friendship> findByUserIdAndFriendshipStat(Long userId, Boolean friendshipStat);
}