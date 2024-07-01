package com.chwipoClova.article.repository;

import com.chwipoClova.article.entity.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    @Query(value = "SELECT u.* FROM (SELECT f.* FROM feed f WHERE f.id IN :ids ORDER BY created_at desc, id) AS u LIMIT :endNumber OFFSET :startNumber",
            nativeQuery = true)
    List<Feed> findFeedByIdsOrderByCreatedAtDescIdAsc(Collection<Long> ids, Integer startNumber, Integer endNumber);

    @Query(value = "SELECT u.* FROM (SELECT f.* FROM feed f WHERE f.id IN :ids ORDER BY created_at desc, id) AS u",
            nativeQuery = true)
    List<Feed> findFeedByIdsOrderByCreatedAtDescIdAsc(Collection<Long> ids);

    @Query(value = "SELECT u.* FROM (SELECT f.* FROM feed f WHERE f.id ORDER BY created_at desc, id) AS u LIMIT :endNumber OFFSET :startNumber",
            nativeQuery = true)
    List<Feed> findFeedAllOrderByCreatedAtDescIdAsc( Integer startNumber, Integer endNumber);

    @Query(value = "SELECT u.* FROM (SELECT f.* FROM feed f WHERE f.id ORDER BY created_at desc, id) AS u",
            nativeQuery = true)
    List<Feed> findFeedAllOrderByCreatedAtDescIdAsc();

}