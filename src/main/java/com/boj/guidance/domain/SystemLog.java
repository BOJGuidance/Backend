package com.boj.guidance.domain;

import com.boj.guidance.util.annotation.LockSerial;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@NoArgsConstructor
public class SystemLog {

    @Id
    @LockSerial
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;          // 접속 사용자
    private String accessAt;        // 접속한 시간
    private String ip;              // 접속한 IP
    private String device;          // 접속한 디바이스
    private String browser;         // 접속한 브라우저
    private String os;              // 접속한 OS

    @Builder
    public SystemLog(
            Member member,
            String ip,
            String device,
            String browser,
            String os
    ) {
        this.member = member;
        this.accessAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        this.ip = ip;
        this.device = device;
        this.browser = browser;
        this.os = os;
    }

}
