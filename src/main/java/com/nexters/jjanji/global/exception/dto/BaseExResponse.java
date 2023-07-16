package com.nexters.jjanji.global.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseExResponse {
    private String message;
    private String status;
}
