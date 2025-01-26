package org.pamdesa.helper;

import org.pamdesa.payload.response.Response;
import org.springframework.http.HttpStatus;

public class ResponseHelper {

    public static <T> Response<T> ok() {
        return ResponseHelper.ok(null);
    }

    public static <T> Response<T> ok(T data) {
        return Response.<T>builder()
                .code(200)
                .status("OK")
                .data(data)
                .build();
    }

    public static <T> Response<T> status(int code, String status) {
        return Response.<T>builder()
                .code(code)
                .status(status)
                .build();
    }
}
