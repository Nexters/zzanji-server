package com.nexters.jjanji.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommonResponse<T> {
    private final T data;

    public CommonResponse(T data) {
        this.data = data;
    }
}
