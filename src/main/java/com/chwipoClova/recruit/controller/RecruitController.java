package com.chwipoClova.recruit.controller;

import com.chwipoClova.common.response.CommonResponse;
import com.chwipoClova.common.response.MessageCode;
import com.chwipoClova.recruit.request.RecruitInsertReq;
import com.chwipoClova.recruit.service.RecruitService;
import com.chwipoClova.resume.request.ResumeDeleteReq;
import com.chwipoClova.resume.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Recruit", description = "이력서 API")
@RequestMapping("recruit")
public class RecruitController {

    private final RecruitService recruitService;

    @Operation(summary = "채용공고 삭제", description = "채용공고 삭제")
    @DeleteMapping(path = "/deleteRecruit")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = String.class)))}
    )
    public CommonResponse deleteRecruit() {
        recruitService.deleteBeforeRecruit();
        return new CommonResponse<>(MessageCode.OK.getCode(), null, MessageCode.OK.getMessage());
    }
}
