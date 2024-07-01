package com.chwipoClova.article.controller;

import com.chwipoClova.article.response.ArticleListRes;
import com.chwipoClova.article.response.FeedCategoryRes;
import com.chwipoClova.article.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Article", description = "아티클 API")
@RequestMapping("article")
public class ArticleController {

    private final ArticleService articleService;

    @Operation(summary = "대분류 목록 조회", description = "대분류 목록 조회")
    @GetMapping(path = "/getMainCategoryList")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    }
    )
    public List<FeedCategoryRes> getMainCategoryList() {
        return articleService.selectMainCategoryList();
    }

    @Operation(summary = "소분류 목록 조회", description = "소분류 목록 조회")
    @GetMapping(path = "/getCategoryList")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    }
    )
    public List<FeedCategoryRes> getSubCategoryList(@Schema(description = "categoryId", example = "1", name = "categoryId") @RequestParam(name = "categoryId") Long categoryId) {
        return articleService.selectSubCategoryList(categoryId);
    }


    @Operation(summary = "아티클 목록 조회", description = "아티클 조회 목록 조회")
    @GetMapping(path = "/getArticleList")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    }
    )
    public List<ArticleListRes> getArticleList(
            @Schema(name = "categoryIdList", type = "array")
            @RequestParam(name = "categoryIdList", required = false) List<Long> categoryIdList,
            @Schema(description = "startNumber", example = "0", name = "startNumber") @RequestParam(name = "startNumber", required = false) Integer startNumber,
            @Schema(description = "endNumber", example = "10", name = "endNumber") @RequestParam(name = "endNumber", required = false) Integer endNumber
    ) {
        return articleService.selectArticleList(categoryIdList, startNumber, endNumber);
    }
}
