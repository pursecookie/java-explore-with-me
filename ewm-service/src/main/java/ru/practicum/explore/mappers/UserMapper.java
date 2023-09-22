package ru.practicum.explore.mappers;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.dto.user.NewUserRequest;
import ru.practicum.explore.dto.user.UserDto;
import ru.practicum.explore.dto.user.UserShortDto;
import ru.practicum.explore.models.User;

@UtilityClass
public class UserMapper {
    public User mapToUser(NewUserRequest newUserRequest) {
        return new User(newUserRequest.getName(), newUserRequest.getEmail());
    }

    public UserDto mapToUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public UserShortDto mapToUserShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }
}