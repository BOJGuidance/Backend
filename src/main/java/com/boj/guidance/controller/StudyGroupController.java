package com.boj.guidance.controller;

import com.boj.guidance.dto.StudyGroupDto.StudyGroupGenerateRequestDto;
import com.boj.guidance.dto.StudyGroupDto.StudyGroupResponseDto;
import com.boj.guidance.service.StudyGroupService;
import com.boj.guidance.util.api.ApiResponse;
import com.boj.guidance.util.api.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/study")
@RequiredArgsConstructor
public class StudyGroupController {

    private final StudyGroupService studyGroupService;

    /**
     * 스터디그룹 생성
     *
     * @param memberId 생성하는 사용자 id
     * @param dto 스터디그룹 목표
     * @return 생성된 스터디그룹 정보
     */
    @PostMapping("/create/{memberId}")
    public ApiResponse<StudyGroupResponseDto> createGroup(@PathVariable("memberId") String memberId,
                                                          @RequestBody StudyGroupGenerateRequestDto dto) {
        return ApiResponse.success(ResponseCode.STUDY_GROUP_CREATE_SUCCESS.getMessage(), studyGroupService.createGroup(memberId, dto));
    }

    /**
     * 스터디그룹 그룹원 모집
     *
     * @param groupId 스터디그룹 id
     * @return 모집이 완료된 스터디그룹 정보
     */
    @PutMapping("/recruit/{groupId}")
    public ApiResponse<StudyGroupResponseDto> recruit(@PathVariable("groupId") String groupId) {
        return ApiResponse.success(ResponseCode.STUDY_GROUP_RECRUIT_SUCCESS.getMessage(), studyGroupService.recruitMember(groupId));
    }

    /**
     * 문제 풀이 완료
     *
     * @param groupId 스터디그룹 id
     * @param problemId 풀이한 문제 id
     * @return 스터디그룹 정보
     */
    @PostMapping("/problem/{groupId}")
    public ApiResponse<StudyGroupResponseDto> solvedProblem(@PathVariable("groupId") String groupId,
                                                            @RequestParam("problemId") Integer problemId) {
        return ApiResponse.success(ResponseCode.STUDY_GROUP_ADD_PROBLEM_SUCCESS.getMessage(), studyGroupService.problemSolved(groupId, problemId));
    }

}
