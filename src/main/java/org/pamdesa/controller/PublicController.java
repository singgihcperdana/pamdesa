package org.pamdesa.controller;

import org.pamdesa.helper.ResponseHelper;
import org.pamdesa.payload.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @GetMapping("/no-auth")
    public Response<String> getNoAuthPage() {
        return ResponseHelper.ok("Welcome to the not authenticated page!");
    }

}
