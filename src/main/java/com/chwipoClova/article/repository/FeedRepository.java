package com.chwipoClova.article.repository;

import com.chwipoClova.article.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    List<Feed> findByCreatedAtBetweenOrderByCreatedAtDesc(Instant createdAtStart, Instant createdAtEnd);

}