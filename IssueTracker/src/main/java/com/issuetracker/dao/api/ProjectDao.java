/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.dao.api;

import com.issuetracker.model.Project;
import java.util.List;

/**
 *
 * @author mgottval
 */
public interface ProjectDao {
    
    void insertProject(Project project);
    
    Project getProjectByName(String name);
    
    List<Project> getProjects();
    
}
