package com.chwipoClova.article.repository;

import com.chwipoClova.article.entity.FeedAndCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface FeedAndCategoryRepository extends JpaRepository<FeedAndCategory, Long> {
    List<FeedAndCategory> findByCategory_IdIn(Collection<Long> ids);
}