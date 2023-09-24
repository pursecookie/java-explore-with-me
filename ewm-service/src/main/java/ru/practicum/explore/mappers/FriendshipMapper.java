package ru.practicum.explore.mappers;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.dto.friendship.FriendshipDto;
import ru.practicum.explore.models.Friendship;

@UtilityClass
public class FriendshipMapper {
    public FriendshipDto mapToFriendshipDto(Friendship friendship) {
        return new FriendshipDto(friendship.getId(),
                friendship.getUser(),
                friendship.getFriend(),
                friendship.getFriendshipStat());
    }
}