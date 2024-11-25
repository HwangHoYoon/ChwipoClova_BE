package com.chwipoClova.article.repository;

import com.chwipoClova.article.entity.Feed;
import com.chwipoClova.article.entity.QFeed;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

import static com.chwipoClova.article.entity.QFeed.feed;

@RequiredArgsConstructor
@Repository
public class FeedRepositoryImpl implements FeedCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Feed> selectFeedList(Collection<Long> ids, Integer startNumber, Integer endNumber) {
        var query = queryFactory.selectFrom(feed).where(idsEq(ids));

        if (startNumber != null) {
            query.offset(startNumber);
        }

        if (endNumber != null) {
            query.limit(endNumber);
        }

        return query.orderBy(feed.createdAt.desc(), feed.id.asc()).fetch();
    }

    @Override
    public List<Feed> selectAllFeedList(Integer startNumber, Integer endNumber) {
        var query = queryFactory.selectFrom(feed);

        if (startNumber != null) {
            query.offset(startNumber);
        }

        if (endNumber != null) {
            query.limit(endNumber);
        }

        return query.orderBy(feed.createdAt.desc(), feed.id.asc()).fetch();
    }

    private BooleanExpression idsEq(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return null;
        }
        return feed.id.in(ids);
    }
}
