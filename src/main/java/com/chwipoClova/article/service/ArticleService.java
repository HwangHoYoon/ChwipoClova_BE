package com.chwipoClova.article.service;

import com.chwipoClova.article.entity.Feed;
import com.chwipoClova.article.entity.FeedAndCategory;
import com.chwipoClova.article.entity.FeedMainCategory;
import com.chwipoClova.article.entity.FeedSubCategory;
import com.chwipoClova.article.repository.FeedAndCategoryRepository;
import com.chwipoClova.article.repository.FeedMainCategoryRepository;
import com.chwipoClova.article.repository.FeedRepository;
import com.chwipoClova.article.repository.FeedSubCategoryRepository;
import com.chwipoClova.article.response.ArticleListRes;
import com.chwipoClova.article.response.FeedCategoryRes;
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

    public List<FeedCategoryRes> selectMainCategoryList() {

        List<FeedMainCategory> feedMainCategoryList = feedMainCategoryRepository.findAll();

        List<FeedCategoryRes> feedCategoryResList = new ArrayList<>();

        feedMainCategoryList.forEach(feedMainCategory -> {
            FeedCategoryRes feedCategoryRes = FeedCategoryRes.builder()
                    .categoryId(feedMainCategory.getId())
                    .categoryName(feedMainCategory.getName())
                    .build();
            feedCategoryResList.add(feedCategoryRes);
        });

        return feedCategoryResList;
    }

    public List<FeedCategoryRes> selectSubCategoryList(Long categoryId) {
        List<FeedSubCategory> feedSubCategoryList =  feedSubCategoryRepository.findByMainId(categoryId);

        List<FeedCategoryRes> feedCategoryResList = new ArrayList<>();

        feedSubCategoryList.forEach(feedMainCategory -> {
            FeedCategoryRes feedCategoryRes = FeedCategoryRes.builder()
                    .categoryId(feedMainCategory.getId())
                    .categoryName(feedMainCategory.getName())
                    .build();
            feedCategoryResList.add(feedCategoryRes);
    });
    return feedCategoryResList;
}

    public List<ArticleListRes> selectArticleList(List<Long> categoryIdList, Integer startNumber, Integer endNumber) {

        List<Feed> feedList = null;

        List<ArticleListRes> articleListResList = new ArrayList<>();

        if (categoryIdList != null && !categoryIdList.isEmpty()) {
            List<FeedAndCategory> feedAndCategoryList = feedAndCategoryRepository.findByCategory_IdIn(categoryIdList);

            List<Long> feedIdList = new ArrayList<>();

            feedAndCategoryList.forEach(feedAndCategory -> {
                Long feedId = feedAndCategory.getFeed().getId();
                feedIdList.add(feedId);
            });

            if (startNumber != null && endNumber != null) {
                feedList = feedRepository.findFeedByIdsOrderByCreatedAtDescIdAsc(feedIdList, startNumber, endNumber);
            } else {
                feedList = feedRepository.findFeedByIdsOrderByCreatedAtDescIdAsc(feedIdList);
            }
        } else {
            if (startNumber != null && endNumber != null) {
                feedList = feedRepository.findFeedAllOrderByCreatedAtDescIdAsc(startNumber, endNumber);
            } else {
                feedList = feedRepository.findFeedAllOrderByCreatedAtDescIdAsc();
            }
        }

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
