package com.BNI.habib.Test.Response;

import java.util.Date;
import java.util.List;

public class CommonResponse {
    public static <T> BaseResponse<T> constructResponse(String code,
                                                        String message, List<String> errors, T data) {
        return new BaseResponse.BaseResponseBuilder<T>()
                .code(code)
                .message(message)
                .errors(errors)
                .serverTime(new Date()).data(data)
                .build();
    }

    private CommonResponse(){}
}
