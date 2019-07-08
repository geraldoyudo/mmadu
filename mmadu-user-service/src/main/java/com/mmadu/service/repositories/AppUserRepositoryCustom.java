package com.mmadu.service.repositories;

import com.mmadu.service.entities.AppUser;
import com.mmadu.service.models.UpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface AppUserRepositoryCustom {

    Page<AppUser> queryForUsers(@Param("query") String query, Pageable p);

    void updateUsers(String query, UpdateRequest updateRequest);
}
