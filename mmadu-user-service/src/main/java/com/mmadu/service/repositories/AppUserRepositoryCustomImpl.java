package com.mmadu.service.repositories;

import com.geraldoyudo.kweeri.mongo.MongoQueryConverter;
import com.geraldoyudo.kweeri.mongo.MongoQuerySerializer;
import com.mmadu.service.entities.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppUserRepositoryCustomImpl implements AppUserRepositoryCustom {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoQuerySerializer querySerializer;

    @Autowired
    private MongoQueryConverter queryConverter;

    @Override
    public Page<AppUser> queryForUsers(String query, Pageable p) {
        String jsonString = queryConverter.convertExpressionToQuery(querySerializer.deSerialize(query));
        BasicQuery basicQuery = new BasicQuery(jsonString);
        basicQuery.with(p.getSort());
        basicQuery.with(p);
        List<AppUser> users = mongoTemplate.find(basicQuery, AppUser.class);
        return PageableExecutionUtils.getPage(
                users,
                p,
                () -> mongoTemplate.count(basicQuery, AppUser.class));
    }
}
