package com.chwipoClova.article.repository;

import com.chwipoClova.article.entity.FeedSubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface FeedSubCategoryRepository extends JpaRepository<FeedSubCategory, Long> {
    List<FeedSubCategory> findByMain_IdOrderByCodeAsc(Long id);
    List<FeedSubCategory> findByCodeIn(Collection<String> codes);
}