/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.component.version;

import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.dao.api.ProjectVersionDao;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;
import java.util.List;
import javax.inject.Inject;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author mgottval
 */
public class VersionListView<T extends ProjectVersion> extends Panel {

    @Inject
    private ProjectVersionDao projectVersionDao;
    @Inject
    private ProjectDao projectDao;
    private List<ProjectVersion> projectVersionList;
    private final ListView<ProjectVersion> versionsListView;

    public VersionListView(String id, IModel<List<ProjectVersion>> versionsModel, final IModel<Project> projectModel) {
        super(id);
        this.projectVersionList = versionsModel.getObject();
        add(new Label("versions", "Versions"));
        versionsListView = new ListView<ProjectVersion>("versionsList", versionsModel) {
            @Override
            protected void populateItem(ListItem<ProjectVersion> item) {
                final ProjectVersion projectVersion = item.getModelObject();
                item.add(new Link<ProjectVersion>("remove", item.getModel()) {
                    @Override
                    public void onClick() {
                        projectVersionList.remove(projectVersion);
                        if (projectModel != null) {
                            Project project = projectDao.getProjectById(projectModel.getObject().getId());
                            project.setVersions(projectVersionList);
                            projectDao.update(project);
                        }
//                        projectVersionDao.remove(projectVersion);
                    }
                });
                item.add(new Label("name", projectVersion.getName()));
            }
        };
        add(versionsListView);
    }

    public List<ProjectVersion> getProjectVersionList() {
        return projectVersionList;
    }

    public void setProjectVersionList(List<ProjectVersion> projectVersionList) {
        this.projectVersionList = projectVersionList;
    }
}
