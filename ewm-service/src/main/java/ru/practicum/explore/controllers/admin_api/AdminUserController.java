package ru.practicum.explore.controllers.admin_api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.user.NewUserRequest;
import ru.practicum.explore.dto.user.UserDto;
import ru.practicum.explore.services.admin_api.AdminUserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class AdminUserController {
    private final AdminUserService adminUserService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<UserDto> readAllUsers(@RequestParam(required = false) List<Long> ids,
                                            @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);

        return adminUserService.readAllUsers(ids, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        return adminUserService.createUser(newUserRequest);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        adminUserService.deleteUser(userId);

        return new ResponseEntity<>("User deleted", HttpStatus.NO_CONTENT);
    }
}