package org.pamdesa.controller.customer;

import lombok.RequiredArgsConstructor;
import org.pamdesa.payload.response.UserInfoResponse;
import org.pamdesa.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final UserService userService;

    @GetMapping("/hello")
    public ResponseEntity<String> getHelloPage() {
        UserInfoResponse current = userService.getCurrent();
        return ResponseEntity.ok("Welcome customer " + current.getUsername() + "!");
    }

}
