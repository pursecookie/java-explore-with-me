package ru.practicum.explore.services.private_api;

import ru.practicum.explore.dto.friendship.FriendshipDto;
import ru.practicum.explore.dto.user.FriendsEventsDto;
import ru.practicum.explore.dto.user.UserShortDto;

import java.util.Collection;

public interface PrivateFriendshipService {
    FriendshipDto addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    Collection<UserShortDto> readAllFriends(Long userId);

    Collection<FriendsEventsDto> readFriendsEvents(Long userId);
}