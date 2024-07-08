package com.chwipoClova.article.repository;

import com.chwipoClova.article.entity.FeedMainCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedMainCategoryRepository extends JpaRepository<FeedMainCategory, Long> {
    Optional<FeedMainCategory> findByCode(String code);

    List<FeedMainCategory> findByCodeIn(List<String> categoryCodeList);
}