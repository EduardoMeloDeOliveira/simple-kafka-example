package io.github.EduardoMeloDeOliveira.user_api.controller;

import io.github.EduardoMeloDeOliveira.user_api.dto.UserRequestDTO;
import io.github.EduardoMeloDeOliveira.user_api.dto.UserResponseDTO;
import io.github.EduardoMeloDeOliveira.user_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        userService.sendCreateUser(userRequestDTO);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable Long userId, @RequestBody UserRequestDTO userRequestDTO) {
        userService.sendUpdateUser(userId, userRequestDTO);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.sendDeleteUser(userId);
        return ResponseEntity.accepted().build();
    }
}
