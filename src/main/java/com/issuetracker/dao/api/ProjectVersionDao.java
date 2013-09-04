/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.dao.api;

import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;
import java.util.List;

/**
 *
 * @author Monika
 */
public interface ProjectVersionDao {
    
    void insertProjectVersion(ProjectVersion projectVersion);
    
    void remove(ProjectVersion projectVersion);
    
    List<ProjectVersion> getProjectVersions();
}
