package ru.practicum.explore.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    @Size(min = 2)
    @Size(max = 250)
    @NotBlank(message = "The name cannot be empty")
    private String name;

    @Size(min = 6)
    @Size(max = 254)
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "The entered email does not match the format of the email address")
    private String email;
}