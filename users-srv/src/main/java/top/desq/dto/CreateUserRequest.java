package top.desq.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    @NotNull(message = "Name cannot be null")
    @Size(min = 2, message = "Name must not be less then 2 characters")
    private String name;

    @NotNull(message = "Email cannot be null")
    @Email
    private String email;

    @NotNull(message = "Password cannot be null")
    @Size(min = 6, max = 10, message = "Password must be equal or greater then 6 and less then 10 characters")
    private String password;
}
