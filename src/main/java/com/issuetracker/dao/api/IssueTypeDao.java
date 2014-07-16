package com.issuetracker.dao.api;

import com.issuetracker.model.IssueType;

import java.util.List;

/**
 *
 * @author mgottval
 */
public interface IssueTypeDao {
    
    void insert(IssueType issueType);
    
    List<IssueType> getIssueTypes();
}
