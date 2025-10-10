package com.rashmita.common.model;

import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SearchResponse {

    <T> PageableResponse<T> getSearchResponse(List<T> record, Integer totalCount);

    <R> PageableResponse<R> getSearchResponse(SearchResponseBuilder<R> searchResponseBuilder);

    <T, R> PageableResponse<R> getSearchResponse(SearchResponseWithMapperBuilder<T, R> responseWithMapperBuilder);
}
