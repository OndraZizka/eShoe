package com.issuetracker.service.api;

import com.issuetracker.model.Component;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;

import java.util.List;

/**
 *
 * @author mgottval
 */
public interface ProjectService {
    
    void insert(Project project);
    
    void update(Project project);
    
    Project getProjectByName(String name);
    
    List<Project> getProjects();
    
    List<ProjectVersion> getProjectVersions(Project project);
    
    List<Component> getProjectComponents(Project project);
    
    void remove(Project project);
    
    Project getProjectById(Long id);
    
    boolean isProjectNameInUse(String projectName);
}
