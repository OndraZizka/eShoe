package com.issuetracker.dao.api;

import com.issuetracker.model.ProjectVersion;

import java.util.List;

/**
 *
 * @author Monika
 */
public interface ProjectVersionDao {
    
    void insert(ProjectVersion projectVersion);
    
    void remove(ProjectVersion projectVersion);
    
    List<ProjectVersion> getProjectVersions();
}
