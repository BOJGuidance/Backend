package com.boj.guidance.dto.MemberDto;

import com.boj.guidance.domain.Member;
import com.boj.guidance.domain.SystemLog;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSystemLogDto {
    private Member member;
    private String ip;
    private String device;
    private String browser;
    private String os;

    @Builder
    public MemberSystemLogDto(
            Member member,
            String ipAddress,
            String userAgent
    ) {
        this.member = member;
        this.ip = ipAddress;
        this.device = getDeviceType(userAgent);
        this.browser = getBrowserType(userAgent);
        this.os = getOperatingSystem(userAgent);
    }

    public SystemLog toEntity() {
        return SystemLog.builder()
                .member(member)
                .ip(ip)
                .device(device)
                .browser(browser)
                .os(os)
                .build();
    }

    public String getDeviceType(String userAgentString) {
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
        switch (userAgent.getOperatingSystem().getDeviceType()) {
            case COMPUTER:
                return "Computer";
            case MOBILE:
                return "Mobile";
            case TABLET:
                return "Tablet";
            case GAME_CONSOLE:
                return "Game Console";
            case DMR:
                return "Digital Media Receiver";
            case WEARABLE:
                return "Wearable";
            case UNKNOWN:
            default:
                return "Unknown";
        }
    }

    public String getBrowserType(String userAgentString) {
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
        return userAgent.getBrowser().getName();
    }

    public String getOperatingSystem(String userAgentString) {
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
        OperatingSystem os = userAgent.getOperatingSystem();
        return os.getName();
    }
}
