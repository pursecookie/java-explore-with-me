package ru.practicum.explore.services.admin_api;

import org.springframework.data.domain.Pageable;
import ru.practicum.explore.dto.user.NewUserRequest;
import ru.practicum.explore.dto.user.UserDto;

import java.util.Collection;
import java.util.List;

public interface AdminUserService {
    Collection<UserDto> readAllUsers(List<Long> ids, Pageable pageable);

    UserDto createUser(NewUserRequest newUserRequest);

    void deleteUser(Long userId);
}