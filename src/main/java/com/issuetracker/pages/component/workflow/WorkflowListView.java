package com.issuetracker.pages.component.workflow;

import com.issuetracker.model.Workflow;
import com.issuetracker.pages.WorkflowDetail;
import com.issuetracker.service.api.WorkflowService;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.inject.Inject;
import java.util.List;

/**
 *
 * @author mgottval
 */
public class WorkflowListView<T extends Workflow> extends Panel {

    @Inject
    private WorkflowService workflowService;
    private List<Workflow> workflows;
    private ListView listViewWorkflows;

    public WorkflowListView(String id, IModel<List<Workflow>> workflowsModel) {
        super(id);
        listViewWorkflows = new ListView<Workflow>("workflowList", workflowsModel) {
            @Override
            protected void populateItem(final ListItem<Workflow> item) {
                final Workflow workflow = item.getModelObject();
                Link workflowDetailLink = new Link<Workflow>("showWorkflow", item.getModel()) {
                    @Override
                    public void onClick() {
                        PageParameters pageParameters = new PageParameters();
                        pageParameters.add("workflow", workflow.getId());
                        setResponsePage(WorkflowDetail.class, pageParameters);
                    }
                };
                workflowDetailLink.add(new Label("name", workflow.getName()));
                item.add(workflowDetailLink);

//                item.add(new Link<Workflow>("remove", item.getModel()) {
//                    @Override
//                    public void onClick() {
//                        workflowService.remove(workflow);
//                        workflows = workflowService.getWorkflows();
//                    }
//                });
            }
        };
        add(listViewWorkflows);
    }

    public List<Workflow> getWorkflows() {
        return workflows;
    }

    public void setWorkflows(List<Workflow> workflows) {
        this.workflows = workflows;
    }
}
