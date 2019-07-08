package com.mmadu.service.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PagedList<T> extends PageImpl<T> {

    public PagedList(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public PagedList(List<T> content) {
        super(content);
    }

    @Override
    @JsonIgnore
    public Pageable getPageable() {
        return super.getPageable();
    }
}
