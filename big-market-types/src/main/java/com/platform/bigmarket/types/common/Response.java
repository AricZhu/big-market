package com.platform.bigmarket.types.common;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Response<T> {
    private String code;
    private String info;

    private T data;

    public static <T> Response<T> success(T data) {
        return Response.<T>builder()
                .code(ResponseCode.RESPONSE_SUCCESS.getCode())
                .info(ResponseCode.RESPONSE_SUCCESS.getDesc())
                .data(data)
                .build();
    }

    public static <T> Response<T> fail(T data, String msg) {
        return Response.<T>builder()
                .code(ResponseCode.RESPONSE_ERROR.getCode())
                .info(msg)
                .data(data)
                .build();
    }
}
