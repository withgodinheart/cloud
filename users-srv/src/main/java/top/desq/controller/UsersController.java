package top.desq.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.desq.dto.CreateUserRequest;
import top.desq.dto.UserResponse;
import top.desq.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    @Value("${token.expiration-time}") // not updates via BUS, stays as it was while startup
    private Long tokenExpirationTime;
    private final Environment env; // updates via BUS
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok("Running on %s port, with token expiration time = %s".formatted(env.getProperty("local.server.port"), env.getProperty("token.expiration-time")));
    }

    @GetMapping("/status")
    public ResponseEntity<?> check() {
        return ResponseEntity.ok("USERS: Running on %s port, with token expiration time %s".formatted(env.getProperty("local.server.port"), tokenExpirationTime));
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest dto) {
        var userDto = userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }
}
