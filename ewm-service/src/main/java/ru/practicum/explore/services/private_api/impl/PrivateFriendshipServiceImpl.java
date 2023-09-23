package ru.practicum.explore.services.private_api.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.component.DataFinder;
import ru.practicum.explore.dto.event.EventShortDto;
import ru.practicum.explore.dto.event_request.ParticipationRequestDto;
import ru.practicum.explore.dto.user.FriendsEventsDto;
import ru.practicum.explore.dto.user.UserShortDto;
import ru.practicum.explore.mappers.EventMapper;
import ru.practicum.explore.mappers.EventRequestMapper;
import ru.practicum.explore.mappers.UserMapper;
import ru.practicum.explore.models.Friendship;
import ru.practicum.explore.models.User;
import ru.practicum.explore.repositories.EventRequestRepository;
import ru.practicum.explore.repositories.FriendshipRepository;
import ru.practicum.explore.services.private_api.PrivateFriendshipService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class PrivateFriendshipServiceImpl implements PrivateFriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final EventRequestRepository eventRequestRepository;
    private final DataFinder dataFinder;

    @Transactional
    @Override
    public void addFriend(Long userId, Long friendId) {
        User user = dataFinder.findUserById(userId);
        User friend = dataFinder.findUserById(friendId);

        Friendship friendship = friendshipRepository.findByUserIdAndFriendId(friendId, userId);

        if (friendship == null) {
            friendshipRepository.save(new Friendship(user, friend, false));
        } else {
            friendship.setFriendshipStat(true);
            friendshipRepository.save(friendship);
            friendshipRepository.save(new Friendship(user, friend, true));
        }
    }

    @Transactional
    @Override
    public void deleteFriend(Long userId, Long friendId) {
        dataFinder.findUserById(userId);
        dataFinder.findUserById(friendId);

        Friendship friendship1 = friendshipRepository.findByUserIdAndFriendId(userId, friendId);
        Friendship friendship2 = friendshipRepository.findByUserIdAndFriendId(friendId, userId);

        if (friendship1 != null) {
            friendship1.setFriendshipStat(false);
            friendshipRepository.save(friendship1);
        }

        if (friendship2 != null) {
            friendship2.setFriendshipStat(false);
            friendshipRepository.save(friendship2);
        }
    }

    @Override
    public Collection<UserShortDto> readAllFriends(Long userId) {
        dataFinder.findUserById(userId);

        Collection<UserShortDto> friends = friendshipRepository.findByUserIdAndFriendshipStat(userId, true)
                .stream()
                .map(Friendship::getFriend)
                .map(UserMapper::mapToUserShortDto)
                .collect(toList());

        return friends.isEmpty() ? new ArrayList<>() : friends;
    }

    @Override
    public Collection<FriendsEventsDto> readFriendsEvents(Long userId) {
        dataFinder.findUserById(userId);

        Collection<UserShortDto> friends = readAllFriends(userId);

        if (friends.isEmpty()) {
            return new ArrayList<>();
        }

        Map<Long, List<ParticipationRequestDto>> friendsRequests = findRequestsByFriend(friends);

        if (friendsRequests.isEmpty()) {
            return new ArrayList<>();
        }

        Collection<FriendsEventsDto> friendsEvents = new ArrayList<>();
        Collection<EventShortDto> confirmedEvents;

        for (UserShortDto friend : friends) {
            confirmedEvents = friendsRequests.get(friend.getId()).stream()
                    .map(ParticipationRequestDto::getEvent)
                    .map(dataFinder::findEventById)
                    .map(EventMapper::mapToEventShortDto)
                    .collect(toList());

            friendsEvents.add(new FriendsEventsDto(friend.getId(), friend.getName(), confirmedEvents));
        }

        return friendsEvents;
    }

    private Map<Long, List<ParticipationRequestDto>> findRequestsByFriend(Collection<UserShortDto> friends) {
        return eventRequestRepository.findConfirmedUserRequests(friends
                        .stream()
                        .map(UserShortDto::getId)
                        .collect(toList()))
                .stream()
                .map(EventRequestMapper::mapToParticipationRequestDto)
                .collect(groupingBy(ParticipationRequestDto::getRequester, toList()));
    }
}