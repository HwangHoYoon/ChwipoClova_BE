package com.chwipoClova.article.repository;

import com.chwipoClova.article.entity.Feed;

import java.util.Collection;
import java.util.List;

public interface FeedCustomRepository {
    List<Feed> selectFeedList(Collection<Long> ids, Integer startNumber, Integer endNumber);
}
