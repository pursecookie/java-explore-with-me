package ru.practicum.explore.services.admin_api.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.user.NewUserRequest;
import ru.practicum.explore.dto.user.UserDto;
import ru.practicum.explore.mappers.UserMapper;
import ru.practicum.explore.models.User;
import ru.practicum.explore.repositories.UserRepository;
import ru.practicum.explore.services.admin_api.AdminUserService;
import ru.practicum.explore.component.DataFinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository userRepository;
    private final DataFinder dataFinder;

    @Override
    public Collection<UserDto> readAllUsers(List<Long> ids, Pageable pageable) {
        if (ids == null) {
            return userRepository.findAll(pageable).stream()
                    .map(UserMapper::mapToUserDto)
                    .collect(Collectors.toList());
        }

        Page<User> list = userRepository.findAllByIdIn(pageable, ids);

        if (list.getContent().isEmpty()) {
            return new ArrayList<>();
        }

        return list.stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserDto createUser(NewUserRequest newUserRequest) {
        User user = UserMapper.mapToUser(newUserRequest);

        return UserMapper.mapToUserDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long userId) {
        dataFinder.findUserById(userId);
        userRepository.deleteById(userId);
    }
}