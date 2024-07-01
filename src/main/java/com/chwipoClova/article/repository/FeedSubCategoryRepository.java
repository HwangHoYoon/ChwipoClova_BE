package com.chwipoClova.article.repository;

import com.chwipoClova.article.entity.FeedSubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedSubCategoryRepository extends JpaRepository<FeedSubCategory, Long> {
    List<FeedSubCategory> findByMainId(Long id);
}