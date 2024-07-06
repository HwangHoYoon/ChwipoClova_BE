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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        FeedMainCategory mainCategory = feedMainCategoryRepository.findByCode(categoryCode).orElseThrow(() -> new CommonException(ExceptionCode.CATEGORY_NULL.getMessage(), ExceptionCode.CATEGORY_NULL.getCode()));
        Long categoryId = mainCategory.getId();

        List<FeedSubCategory> feedSubCategoryList =  feedSubCategoryRepository.findByMain_IdOrderByCodeAsc(categoryId);
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

        if (categoryCodeList != null && !categoryCodeList.isEmpty()) {
            List<Long> categoryIdList = new ArrayList<>();
            List<FeedSubCategory> subCategoryList = feedSubCategoryRepository.findByCodeIn(categoryCodeList);
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
