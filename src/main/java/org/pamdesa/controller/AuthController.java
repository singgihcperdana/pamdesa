package org.pamdesa.controller;

import lombok.RequiredArgsConstructor;
import org.pamdesa.helper.ResponseHelper;
import org.pamdesa.model.constant.AppPath;
import org.pamdesa.model.payload.request.LoginRequest;
import org.pamdesa.model.payload.response.Response;
import org.pamdesa.model.payload.response.UserInfoResponse;
import org.pamdesa.service.AuthService;
import org.pamdesa.service.UserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final UserService userService;

    private final AuthService authService;

    @PostMapping(AppPath.LOGIN)
    public Response<String> login(@Validated @RequestBody LoginRequest request) {
        try {
            return ResponseHelper.ok(authService.login(request));
        } catch (BadCredentialsException ex) {
            return ResponseHelper.status(400, "BadCredential");
        }
    }

    @GetMapping(AppPath.CURRENT)
    public Response<UserInfoResponse> current() {
        return ResponseHelper.ok(userService.getCurrent());
    }

    @PostMapping(AppPath.LOGOUT)
    public Response<String> logout(@RequestHeader("token") String token) {
        authService.logout(token);
        return ResponseHelper.ok();
    }

}
