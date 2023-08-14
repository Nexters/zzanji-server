package com.nexters.jjanji.infra.pushnotification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PushMessage {
    CONSUMPTION_HISTORY("소비 내역을 입력하셨나요?", "입력하지 않았다면, 지금 바로 입력해주세요!")
    ;
    String title;
    String content;
}
