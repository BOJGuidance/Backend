package com.boj.guidance.dto.MemberDto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginRequestDto {
    private String loginId;
    private String loginPassword;
}
