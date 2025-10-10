package com.rashmita.common.repository;

import com.rashmita.common.model.SearchParam;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRepository<T> {

    Long count(SearchParam searchParam);
    List<T> getAll(SearchParam searchParam);
}
