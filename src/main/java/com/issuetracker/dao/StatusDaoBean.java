package com.issuetracker.dao;

import com.issuetracker.dao.api.StatusDao;
import com.issuetracker.model.Issue;
import com.issuetracker.model.Status;
import com.issuetracker.model.Transition;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mgottval
 */
@Stateless
public class StatusDaoBean implements StatusDao{

    @PersistenceContext
    private EntityManager em;
    private CriteriaBuilder qb;
    
    @Override
    public void insert(Status status) {
        em.persist(status);
    }

    @Override
    public List<Status> getStatuses() {
        qb = em.getCriteriaBuilder();
        CriteriaQuery<Status> c = qb.createQuery(Status.class);
        Root<Status> u = c.from(Status.class);
        TypedQuery<Status> pQuery = em.createQuery(c);
        List<Status> results = pQuery.getResultList();
        if (results != null && !results.isEmpty()) {
            return results;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void remove(Status status) {
        em.remove(em.contains(status)? status : em.merge(status));
    }

    @Override
    public Status getStatusById(Long id) {
        qb = em.getCriteriaBuilder();
        CriteriaQuery<Status> statusQuery = qb.createQuery(Status.class);
        Root<Status> p = statusQuery.from(Status.class);
        Predicate pCondition = qb.equal(p.get("id"), id);
        statusQuery.where(pCondition);
        TypedQuery<Status> pQuery = em.createQuery(statusQuery);
        List<Status> statusResults = pQuery.getResultList();
        if (statusResults != null && !statusResults.isEmpty()) {
            return statusResults.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Status getStatusByName(String name) {
        qb = em.getCriteriaBuilder();
        CriteriaQuery<Status> statusQuery = qb.createQuery(Status.class);
        Root<Status> p = statusQuery.from(Status.class);
        Predicate pCondition = qb.equal(p.get("name"), name);
        statusQuery.where(pCondition);
        TypedQuery<Status> pQuery = em.createQuery(statusQuery);
        List<Status> statusResults = pQuery.getResultList();
        if (statusResults != null && !statusResults.isEmpty()) {
            return statusResults.get(0);
        } else {
            return null;
        }
    }

    @Override
    public boolean isStatusUsed(Status status) {
        qb = em.getCriteriaBuilder();
        
        CriteriaQuery query = qb.createQuery();
        
        Root<Issue> i = query.from(Issue.class);
        Root<Transition> t = query.from(Transition.class);
        
        query.multiselect(i, t);
        query.where(
                qb.or(
                        qb.equal(i.get("status"), status.getId()), 
                        qb.equal(t.get("fromStatus"), status.getId()), 
                        qb.equal(t.get("toStatus"), status.getId())
                )
        );
        TypedQuery createQuery = em.createQuery(query);
        return !createQuery.getResultList().isEmpty();
    }

    @Override
    public void update(Status status) {
        em.merge(status);
    }
}
