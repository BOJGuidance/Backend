package com.boj.guidance.domain;

import com.boj.guidance.util.annotation.LockSerial;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@NoArgsConstructor
public class SystemAccess {

    @Id
    @LockSerial
    private String id;
    private String memberId;        // 접속 사용자 id
    private String memberHandle;    // 접속 사용자 이름
    private String accessAt;        // 접속한 시간
    private String ip;              // 접속한 IP
    private String device;          // 접속한 디바이스
    private String browser;         // 접속한 브라우저

    @Builder
    public SystemAccess(
            String memberId,
            String memberHandle,
            String ip,
            String device,
            String browser
    ) {
        this.memberId = memberId;
        this.memberHandle = memberHandle;
        this.accessAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        this.ip = ip;
        this.device = device;
        this.browser = browser;
    }

}
