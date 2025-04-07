package de.ait.patientappointmentsystem.dto;

import de.ait.patientappointmentsystem.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class UpdateUserDto {

    @NotBlank(message = "Username must not be blank")
    private String username;

    @NotBlank(message = "Password must not be blank")
    private String password;

    @NotNull(message = "Roles cannot be null")
    @NotEmpty(message = "At least one role must be assigned")
    private Set<Role> roles;
}
