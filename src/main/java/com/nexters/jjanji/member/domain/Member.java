package com.nexters.jjanji.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "MEMBER", indexes = {
        @Index(name = "IDX_MEMBER_DEVICE_ID", columnList = "DEVICE_ID", unique = true)
})
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(unique = true, nullable = false)
    private String deviceId;

    @Builder
    public Member(Long id, String deviceId) {
        this.id = id;
        this.deviceId = deviceId;
    }
}
