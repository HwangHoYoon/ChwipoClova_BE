package com.chwipoClova.article.service;

import com.chwipoClova.article.entity.Feed;
import com.chwipoClova.article.entity.FeedAndCategory;
import com.chwipoClova.article.entity.FeedMainCategory;
import com.chwipoClova.article.entity.FeedSubCategory;
import com.chwipoClova.article.repository.*;
import com.chwipoClova.article.response.ArticleListRes;
import com.chwipoClova.article.response.FeedCategoryRes;
import com.chwipoClova.common.exception.CommonException;
import com.chwipoClova.common.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ArticleService {

    private final FeedMainCategoryRepository feedMainCategoryRepository;
    private final FeedSubCategoryRepository feedSubCategoryRepository;
    private final FeedAndCategoryRepository feedAndCategoryRepository;
    private final FeedRepository feedRepository;
    private final FeedCustomRepository feedCustomRepository;

    public List<FeedCategoryRes> selectMainCategoryList() {

        List<FeedMainCategory> feedMainCategoryList = feedMainCategoryRepository.findAll();

        List<FeedCategoryRes> feedCategoryResList = new ArrayList<>();

        feedMainCategoryList.forEach(feedMainCategory -> {
            FeedCategoryRes feedCategoryRes = FeedCategoryRes.builder()
                    .categoryId(feedMainCategory.getCode())
                    .categoryName(feedMainCategory.getName())
                    .build();
            feedCategoryResList.add(feedCategoryRes);
        });

        return feedCategoryResList;
    }

    public List<FeedCategoryRes> selectSubCategoryList(String categoryCode) {
        List<FeedSubCategory> feedSubCategoryList;
        if (categoryCode != null && !categoryCode.isEmpty()) {
            FeedMainCategory mainCategory = feedMainCategoryRepository.findByCode(categoryCode).orElseThrow(() -> new CommonException(ExceptionCode.CATEGORY_NULL.getMessage(), ExceptionCode.CATEGORY_NULL.getCode()));
            Long categoryId = mainCategory.getId();
            feedSubCategoryList =  feedSubCategoryRepository.findByMain_IdOrderByCodeAsc(categoryId);
        } else {
            feedSubCategoryList = feedSubCategoryRepository.findAllByOrderByMain_IdAscCode();
        }

        List<FeedCategoryRes> feedCategoryResList = new ArrayList<>();

        feedSubCategoryList.forEach(feedMainCategory -> {
            FeedCategoryRes feedCategoryRes = FeedCategoryRes.builder()
                    .categoryId(feedMainCategory.getCode())
                    .categoryName(feedMainCategory.getName())
                    .build();
            feedCategoryResList.add(feedCategoryRes);
    });
    return feedCategoryResList;
}

    public List<ArticleListRes> selectArticleList(List<String> categoryCodeList, Integer startNumber, Integer endNumber) {

        List<ArticleListRes> articleListResList = new ArrayList<>();
        List<Long> feedIdList = new ArrayList<>();

        List<FeedSubCategory> mainSubCategoryList = new ArrayList<>();
        if (categoryCodeList != null && !categoryCodeList.isEmpty()) {
            // 대분류 카테고리 있는지 확인하여 소분류 카테고리 조회
            List<FeedMainCategory> mainCategoryList = feedMainCategoryRepository.findByCodeIn(categoryCodeList);
            if (mainCategoryList != null && !mainCategoryList.isEmpty()) {
                List<Long> mainCategoryIdList = new ArrayList<>();
                mainCategoryList.forEach(feedMainCategory -> {
                    Long categoryId = feedMainCategory.getId();
                    mainCategoryIdList.add(categoryId);
                });
                if (!mainCategoryIdList.isEmpty()) {
                    mainSubCategoryList = feedSubCategoryRepository.findByMain_IdIn(mainCategoryIdList);
                }
            }

            // 소분류 카테고리 조회
            List<Long> categoryIdList = new ArrayList<>();
            List<FeedSubCategory> subCategoryList = feedSubCategoryRepository.findByCodeIn(categoryCodeList);

            if (!mainSubCategoryList.isEmpty()) {
                subCategoryList.addAll(mainSubCategoryList);
            }

            if (subCategoryList == null || subCategoryList.isEmpty()) {
                return new ArrayList<>();
            }

            subCategoryList.forEach(feedSubCategory -> categoryIdList.add(feedSubCategory.getId()));
            List<FeedAndCategory> feedAndCategoryList = feedAndCategoryRepository.findByCategory_IdIn(categoryIdList);
            if (feedAndCategoryList == null || feedAndCategoryList.isEmpty()) {
                return new ArrayList<>();
            }
            feedAndCategoryList.forEach(feedAndCategory -> {
                Long feedId = feedAndCategory.getFeed().getId();
                feedIdList.add(feedId);
            });
        }

        List<Feed> feedList = feedCustomRepository.selectFeedList(feedIdList, startNumber, endNumber);

        feedList.forEach(feed -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            ArticleListRes articleListRes = ArticleListRes.builder()
                    .id(feed.getId())
                    .title(feed.getTitle())
                    .description(feed.getDescription())
                    .companyName(feed.getCompany().getName())
                    .guid(feed.getGuid())
                    .link(feed.getLink())
                    .thumbnail(feed.getThumbnail())
                    .published(LocalDateTime.ofInstant(feed.getPublished(), ZoneId.systemDefault()).format(formatter))
                    .createdAt(LocalDateTime.ofInstant(feed.getCreatedAt(), ZoneId.systemDefault()).format(formatter))
                    .build();
            articleListResList.add(articleListRes);
        });

        return articleListResList;
    }
}
