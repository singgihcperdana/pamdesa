package org.pamdesa.controller;

import lombok.RequiredArgsConstructor;
import org.pamdesa.model.constant.AppPath;
import org.pamdesa.model.payload.response.UserInfoResponse;
import org.pamdesa.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final UserService userService;

    @GetMapping(AppPath.CUSTOMER_HELLO)
    public ResponseEntity<String> getHelloPage() {
        UserInfoResponse current = userService.getCurrent();
        return ResponseEntity.ok("Welcome customer " + current.getUsername() + "!");
    }

    @PostMapping(AppPath.CUSTOMER_HELLO)
    public ResponseEntity<String> postHelloPage() {
        UserInfoResponse current = userService.getCurrent();
        return ResponseEntity.ok("Welcome customer " + current.getUsername() + "!");
    }

    @GetMapping(AppPath.CUSTOMER_ORDER)
    public ResponseEntity<String> getHelloPage(@PathVariable("id") String id) {
        return ResponseEntity.ok("order " + id);
    }

}
