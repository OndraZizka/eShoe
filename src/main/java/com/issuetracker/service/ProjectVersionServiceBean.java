package com.issuetracker.service;

import com.issuetracker.dao.api.ProjectVersionDao;
import com.issuetracker.model.ProjectVersion;
import com.issuetracker.service.api.ProjectVersionService;
import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

/**
 *
 * @author Monika
 */
@Stateless
public class ProjectVersionServiceBean implements ProjectVersionService, Serializable {

    @Inject
    private ProjectVersionDao projectVersionDao;

    @Override
    public void insert(ProjectVersion projectVersion) {
        projectVersionDao.insert(projectVersion);
    }

    @Override
    public void remove(ProjectVersion projectVersion) {
        projectVersionDao.remove(projectVersion);
    }
    
    @Override
    public List<ProjectVersion> getProjectVersions() {
        return projectVersionDao.getProjectVersions();
    }
}
