package top.desq.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final ServletWebServerApplicationContext ctx;

    @GetMapping()
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok("Running on %s port".formatted(ctx.getWebServer().getPort()));
    }

//    @GetMapping
//    public ResponseEntity<?> without() {
//        return ResponseEntity.ok("Without params");
//    }

//    @GetMapping("")
//    public ResponseEntity<?> empty() {
//        return ResponseEntity.ok("Empty string");
//    }

//    @GetMapping("/")
//    public ResponseEntity<?> slash() {
//        return ResponseEntity.ok("Slash string");
//    }
}
