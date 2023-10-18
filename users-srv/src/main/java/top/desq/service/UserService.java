package top.desq.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import top.desq.dto.CreateUserRequest;
import top.desq.dto.UserResponse;

public interface UserService extends UserDetailsService {

    UserResponse create(CreateUserRequest dto);

    UserResponse getUserByEmail(String email);
}
