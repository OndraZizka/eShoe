/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.dao;

import com.issuetracker.dao.api.IssueDao;
import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.model.Issue;
import com.issuetracker.model.Project;
import com.issuetracker.pages.ListIssues;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;

/**
 *
 * @author mgottval
 */
@Stateless
public class IssueDaoBean implements IssueDao {

    @PersistenceContext(unitName = "com_IssueTracker_war_1.0-SNAPSHOTPU2")
    private EntityManager em;
    private CriteriaBuilder qb;
    @Inject
    private ProjectDao projectDao;

    @Override
    public List<Issue> getIssues() {
        qb = em.getCriteriaBuilder();
        CriteriaQuery<Issue> c = qb.createQuery(Issue.class);
        Root<Issue> i = c.from(Issue.class);
        TypedQuery<Issue> q = em.createQuery(c);
        return q.getResultList();
        //  return em.createQuery("SELECT i FROM Issue i").getResultList();

    }

    @Override
    public Issue getIssueById(Long id) {
        qb = em.getCriteriaBuilder();
        CriteriaQuery<Issue> c = qb.createQuery(Issue.class);
        Root<Issue> i = c.from(Issue.class);
        Predicate condition = qb.equal(i.get("issueId"), id);
        c.where(condition);
        TypedQuery<Issue> q = em.createQuery(c);
        List<Issue> results = q.getResultList();

        // List<Issue> results = em.createQuery("SELECT i FROM Issue i WHERE id = :id", Issue.class).setParameter("id", id).getResultList();
        if (results != null && !results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }

//     @Override
//     public List<Issue> getIssuesByProjectName(String projectName) {
//        TypedQuery<Issue> q = em.createQuery("SELECT i FROM Issue i WHERE i.project.name = :projectName", Issue.class)
//                .setParameter("projectName", projectName);
//        List<Issue> results = q.getResultList();
//        if (!results.isEmpty() && results!=null) {
//            String s = null;
//                    for (Issue issue : results) {
//                        s = issue.getName();
//                    }
//                    Logger.getLogger(ListIssues.class.getName()).log(Level.SEVERE, s);
//                    }
//                    
//                else{
//                    Logger.getLogger(ListIssues.class.getName()).log(Level.SEVERE, "empty");
//        }
//        if (results != null && !results.isEmpty()) {
//            return results;
//        }
//        return null;
//    }
    
    @Override
    public List<Issue> getIssuesByProjectName(String projectName) {
        qb = em.getCriteriaBuilder();
        Logger.getLogger(ListIssues.class.getName()).log(Level.SEVERE, projectName + "DAO");
        CriteriaQuery<Issue> c = qb.createQuery(Issue.class);
        Root<Issue> i = c.from(Issue.class);
c.select(i);
c.where(
        qb.equal( i.get( "project" ).get( "name" ), projectName )
    
);
        Logger.getLogger(ListIssues.class.getName()).log(Level.SEVERE, projectDao.getProjectByName(projectName).getName()+ " get name of project");
//        Metamodel model = em.getMetamodel();
//ManagedType farmType = model.managedType(Issue.class);
//        Attribute animalAttr = farmType.getAttribute("project");
//        Join animalJoin = i.join((ListAttribute)animalAttr);
//        Path nameField = animalJoin.get("name");
//        Predicate condition = qb.equal(nameField, projectName);
//        
//        Predicate condition = qb.equal(i.get("project").get("name"), projectName);
//        c.where(condition);
//        TypedQuery<Issue> q = em.createQuery(c);
//        List<Issue> results = q.getResultList();
        List<Issue> results = em.createQuery(c).getResultList();
         if (!results.isEmpty() && results!=null) {
            String s = null;
                    for (Issue issue : results) {
                        s = issue.getName();
                    }
                    Logger.getLogger(ListIssues.class.getName()).log(Level.SEVERE, s+" string s in DAO");
                    }
                    
                else{
                    Logger.getLogger(ListIssues.class.getName()).log(Level.SEVERE, "empty");
        }
        if (results != null && !results.isEmpty()) {
            return results;
        }
        return null;
    }

    @Override
    public Issue getIssueByName(String name) {
        qb = em.getCriteriaBuilder();
        CriteriaQuery<Issue> c = qb.createQuery(Issue.class);
        Root<Issue> i = c.from(Issue.class);
        Predicate condition = qb.equal(i.get("name"), name);
        c.where(condition);
        TypedQuery<Issue> q = em.createQuery(c);
        List<Issue> results = q.getResultList();

        if (results != null && !results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }

    @Override
    public void addIssue(Issue issue) {
        em.persist(issue);
    }

    @Override
    public Issue updateIssue(Issue issue) {
        Issue updatedIssue = em.merge(issue);
        return updatedIssue;
    }

    @Override
    public void removeIssue(Issue issue) {
        em.remove(issue);
    }

    @Override
    public List<Issue> getIssuesByProject(Project project) {
        qb = em.getCriteriaBuilder();
        CriteriaQuery<Issue> c = qb.createQuery(Issue.class);
        Root<Issue> i = c.from(Issue.class);
        Predicate condition = qb.equal(i.get("project"), project);
        c.where(condition);
        TypedQuery<Issue> q = em.createQuery(c);
        List<Issue> results = q.getResultList();

        if (results != null && !results.isEmpty()) {
            return results;
        }
        return null;
    }
}