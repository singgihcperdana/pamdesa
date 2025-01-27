package org.pamdesa.controller.internal;

import org.pamdesa.helper.ResponseHelper;
import org.pamdesa.model.constant.AppPath;
import org.pamdesa.payload.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InternalController {

    @GetMapping(AppPath.ADMIN_HELLO)
    public Response<String> getHelloPage() {
        return ResponseHelper.ok("Welcome internal!");
    }

}
