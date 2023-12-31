package ru.practicum.explore.controllers.private_api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.friendship.FriendshipDto;
import ru.practicum.explore.dto.user.FriendsEventsDto;
import ru.practicum.explore.dto.user.UserShortDto;
import ru.practicum.explore.services.private_api.PrivateFriendshipService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/friends")
public class PrivateFriendshipController {
    private final PrivateFriendshipService privateFriendshipService;

    @PostMapping("/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public FriendshipDto addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        return privateFriendshipService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        privateFriendshipService.deleteFriend(userId, friendId);

        return new ResponseEntity<>("Friend deleted", HttpStatus.NO_CONTENT);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<UserShortDto> readAllFriends(@PathVariable Long userId) {
        return privateFriendshipService.readAllFriends(userId);
    }

    @GetMapping("/feed")
    @ResponseStatus(HttpStatus.OK)
    public Collection<FriendsEventsDto> readFriendsEvents(@PathVariable Long userId) {
        return privateFriendshipService.readFriendsEvents(userId);
    }
}