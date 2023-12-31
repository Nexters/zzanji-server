package com.nexters.jjanji.domain.member.domain;

import com.nexters.jjanji.global.domain.BaseTime;
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
@Table(name = "member", indexes = {
        @Index(name = "idx_member_device_id", columnList = "device_id", unique = true)
})
public class Member extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "device_id", nullable = false, unique = true)
    private String deviceId;

    @Builder
    public Member(Long id, String deviceId) {
        this.id = id;
        this.deviceId = deviceId;
    }
}
