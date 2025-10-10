package com.rashmita.systemservice.repository;

import com.rashmita.systemservice.entity.AccessGroup;
import com.rashmita.systemservice.model.SearchParam;
import com.rashmita.systemservice.model.SearchParamUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.rashmita.systemservice.constants.ApiConstants.NAME;
import static com.rashmita.systemservice.constants.ApiConstants.STATUS;

@Repository
@RequiredArgsConstructor
public class AccessGroupSearchRepositoryImpl implements AccessGroupSearchRepository {
    @PersistenceContext
    protected EntityManager em;

    @Override
    public Long count(SearchParam searchParam) {
        return (Long) em.createQuery("select COUNT(ag.id) " +
                        "from AccessGroup  ag " +
                        "join Status s on s.id=ag.status.id " +
                        " where " +
                        "(:name is null or ag.name like CONCAT('%', :name, '%')) and " +
                        "(:status is null or s.description=:status) ")
                .setParameter("name", SearchParamUtil.getString(searchParam, NAME))
                .setParameter("status", SearchParamUtil.getString(searchParam, STATUS))
                .getSingleResult();
    }

    @Override
    public List<AccessGroup> getAll(SearchParam searchParam) {
        return em.createQuery("select ag " +
                        "from AccessGroup  ag " +
                        "join Status s on s.id=ag.status.id " +
                        " where " +
                        "(:name is null or ag.name like CONCAT('%', :name, '%')) and " +
                        "(:status is null or s.description=:status) ")
                .setParameter("name", SearchParamUtil.getString(searchParam, NAME))
                .setParameter("status", SearchParamUtil.getString(searchParam, STATUS))
                .getResultList();
    }
}
