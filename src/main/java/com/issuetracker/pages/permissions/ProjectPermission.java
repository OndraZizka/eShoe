package com.issuetracker.pages.permissions;

import com.issuetracker.model.Project;
import com.issuetracker.model.Role;
import com.issuetracker.model.TypeId;
import com.issuetracker.pages.component.permission.PermissionListView;
import com.issuetracker.pages.component.permission.VisibilityProjectForm;
import com.issuetracker.pages.layout.PageLayout;
import com.issuetracker.pages.project.ProjectDetail;
import com.issuetracker.service.api.ProjectService;
import com.issuetracker.service.api.RoleService;
import static com.issuetracker.web.Constants.HOME_PAGE;
import javax.inject.Inject;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.jboss.logging.Logger;

/**
 *
 * @author vramik
 */
public class ProjectPermission extends PageLayout {

    private final Logger log = Logger.getLogger(ProjectPermission.class);
    
    @Inject 
    RoleService roleService;
    @Inject 
    ProjectService projectService;
    
    public ProjectPermission(PageParameters parameters) {
        StringValue stringProjectId = parameters.get("project");
        if (stringProjectId.equals(StringValue.valueOf((String)null))) {
            throw new RedirectToUrlException(HOME_PAGE);
        }
        final Long projectId = stringProjectId.toLong();
        Project project = projectService.getProjectById(projectId);
        
        add(new Label("permissionTitle", "Permissions for project " + project.getName()));
        add(new VisibilityProjectForm("visibilityProjectForm", project));
        
        ListView<Role> permissions = new ListView<Role>("permissions", roleService.getRoles()) {
            
            @Override
            protected void populateItem(ListItem<Role> item) {
                item.add(new PermissionListView("permission", item.getModelObject(), TypeId.project, projectId));
            }
        };
        add(permissions);
        
        add(new Link("projectDetail") {
            @Override
            public void onClick() {
                PageParameters pageParameters = new PageParameters();
                pageParameters.add("project", projectId);
                setResponsePage(ProjectDetail.class, pageParameters);
            }
        });
    }
}

