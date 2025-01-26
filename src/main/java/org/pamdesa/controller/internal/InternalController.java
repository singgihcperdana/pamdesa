package org.pamdesa.controller.internal;

import org.pamdesa.helper.ResponseHelper;
import org.pamdesa.payload.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal")
public class InternalController {

    @GetMapping("/hello")
    public Response<String> getHelloPage() {
        return ResponseHelper.ok("Welcome internal!");
    }

}
