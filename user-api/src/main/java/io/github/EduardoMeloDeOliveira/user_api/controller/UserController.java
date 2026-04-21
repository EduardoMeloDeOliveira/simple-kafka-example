package io.github.EduardoMeloDeOliveira.user_api.controller;

import io.github.EduardoMeloDeOliveira.user_api.dto.UserRequestDTO;
import io.github.EduardoMeloDeOliveira.user_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;


    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody  UserRequestDTO userRequestDTO) {

        userService.sendMessageCreateUser(userRequestDTO);
        return ResponseEntity.created(null).build();
    }

}
