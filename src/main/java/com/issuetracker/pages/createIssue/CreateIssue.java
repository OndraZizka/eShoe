package com.issuetracker.pages.createIssue;

import com.issuetracker.model.*;
import com.issuetracker.pages.IssueDetail;
import com.issuetracker.pages.PageLayout;
import com.issuetracker.service.api.*;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.lang.Bytes;

import javax.inject.Inject;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mgottval
 */
public class CreateIssue extends PageLayout {

    @Inject
    private IssueService issueService;
    @Inject
    private StatusService statusService;
    @Inject
    private ProjectService projectService;
    @Inject
    private IssueTypeService issueTypeService;
    @Inject
    private ComponentService componentService;
    private Form<Issue> insertIssueForm;
    private DropDownChoice<Project> projectList;
    private DropDownChoice<IssueType> issueTypeList;
    private ListMultipleChoice<Component> listComponents;
    private FileUploadField fileUploadField;
//    private DropDownChoice<Priority> priorityList;
    private Issue issue;
    private List<Issue> issueList;
    private Project selectedProject;
    private List<CustomField> projectCustomFieldList;
    private final Map<Project, List<Component>> modelsProjectMap = new HashMap<Project, List<Component>>();
    private final Map<Project, List<ProjectVersion>> modelsProjectVersionsMap = new HashMap<Project, List<ProjectVersion>>();
    private CreateIssueCustomFIeldsListView cfListView;
    private List<CustomFieldIssueValue> cfIssueValues;

    public CreateIssue() {
        issueList = new ArrayList<Issue>();
        List<Project> projects = projectService.getProjects();
        for (Project p : projects) {
            modelsProjectMap.put(p, p.getComponents());
        }
        for (Project p : projects) {
            modelsProjectVersionsMap.put(p, p.getVersions());
        }
        //model of projects list
        IModel<List<Project>> makeChoices2 = new AbstractReadOnlyModel<List<Project>>() {
            @Override
            public List<Project> getObject() {
                List<Project> list = new ArrayList<Project>(modelsProjectMap.keySet());
                return list;
            }
        };//model of components of selected project
        IModel<List<Component>> modelChoices2 = new AbstractReadOnlyModel<List<Component>>() {
            @Override
            public List<Component> getObject() {
                List<Component> models = modelsProjectMap.get(selectedProject);
                if (models == null) {
                    models = Collections.emptyList();
                }
                return models;
            }
        };
        IModel<List<ProjectVersion>> modelChoicesVersions = new AbstractReadOnlyModel<List<ProjectVersion>>() {
            @Override
            public List<ProjectVersion> getObject() {
                List<ProjectVersion> models = modelsProjectVersionsMap.get(selectedProject);
                if (models == null) {
                    models = Collections.emptyList();
                }
                return models;
            }
        };

        IModel<List<Issue.Priority>> priorityModelChoices = new AbstractReadOnlyModel<List<Issue.Priority>>() {
            @Override
            public List<Issue.Priority> getObject() {
                List<Issue.Priority> models = new ArrayList<Issue.Priority>();
                models.add(Issue.Priority.LOW);
                models.add(Issue.Priority.HIGH);
                models.add(Issue.Priority.MEDIUM);
                models.add(Issue.Priority.MEDIUM_HIGH);
                models.add(Issue.Priority.MEDIUM_LOW);
                return models;
            }
        };


        IModel<List<CustomFieldIssueValue>> customFieldsModel = new PropertyModel<List<CustomFieldIssueValue>>(this, "cfIssueValues") {
            @Override
            public List<CustomFieldIssueValue> getObject() {
                if (selectedProject == null) {
                    projectCustomFieldList = new ArrayList<CustomField>();
                } else {
                    projectCustomFieldList = selectedProject.getCustomFields();
                }
                cfIssueValues = new ArrayList<CustomFieldIssueValue>();
                CustomFieldIssueValue cfIssueValue;
                for (CustomField customField : projectCustomFieldList) {
                    cfIssueValue = new CustomFieldIssueValue();
                    cfIssueValue.setCustomField(customField);
                    cfIssueValue.setIssue(issue);
                    cfIssueValues.add(cfIssueValue);
                }
                return cfIssueValues;
            }
        };


        cfListView = new CreateIssueCustomFIeldsListView("cfListView", customFieldsModel);
        cfListView.setOutputMarkupId(true);
        insertIssueForm = new Form("form") {
            @Override
            protected void onSubmit() {
                FileUpload fileUpload = fileUploadField.getFileUpload();
                if (fileUpload != null) {
                    File file = null;
                    try {
                        file = new File("/home/mgottval/TEST-GATEINplug/" + fileUpload.getClientFileName());
                        fileUpload.writeTo(file);
                        Logger.getLogger(CreateIssue.class.getName()).log(Level.SEVERE, fileUpload.getClientFileName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    issue.setFileLocation(file.getAbsolutePath());
                }

                if(issueService.getIssues().isEmpty()) {
                    if (statusService.getStatusByName("New")== null){
                    Status newStatus = new Status("New");
                    statusService.insert(newStatus);
                    }
                    if (statusService.getStatusByName("Modified")== null){
                    Status modifiedStatus = new Status("Modified");
                    statusService.insert(modifiedStatus);
                    }
                    if (statusService.getStatusByName("Closed")==null){
                    Status closedStatus = new Status("Closed");                    
                    statusService.insert(closedStatus);
                    }
                }
                issue.setStatus(statusService.getStatusByName("New"));
                issue.setProject(selectedProject);
                issue.setCustomFields(cfIssueValues);
                issueService.insert(issue);
                issueList = issueService.getIssues();
                PageParameters pageParameters = new PageParameters();
                pageParameters.add("issue", issue.getIssueId());
                setResponsePage(IssueDetail.class, pageParameters);
                issue = new Issue();

            }
        };
        add(insertIssueForm);
        final DropDownChoice<Project> projectDropDown = new DropDownChoice<Project>("projectDropDown", new PropertyModel<Project>(this, "selectedProject"), makeChoices2, new ChoiceRenderer<Project>("name"));
        projectDropDown.setMarkupId("projectDD");
        projectDropDown.setNullValid(false);
//        if (projectDropDown.getChoices().get(0) != null) {
//            projectDropDown.setModelObject(projectDropDown.getChoices().get(0));
//        }
        projectDropDown.setRequired(true);
        final DropDownChoice<Component> componentDropDown = new DropDownChoice<Component>("componentDropDown", new PropertyModel<Component>(this, "issue.component"), modelChoices2, new ChoiceRenderer<Component>("name"));
        componentDropDown.setRequired(true);
        final DropDownChoice<ProjectVersion> versionDropDown = new DropDownChoice<ProjectVersion>("versionsDropDown", new PropertyModel<ProjectVersion>(this, "issue.projectVersion"), modelChoicesVersions, new ChoiceRenderer<ProjectVersion>("name"));
        versionDropDown.setRequired(true);
        final DropDownChoice<Issue.Priority> priorityDropDown = new DropDownChoice<Issue.Priority>("priorityDropDown", new PropertyModel<Issue.Priority>(this, "issue.priority"), priorityModelChoices);


        componentDropDown.setOutputMarkupId(true);
        versionDropDown.setOutputMarkupId(true);
        insertIssueForm.add(projectDropDown);
        insertIssueForm.add(componentDropDown);
        insertIssueForm.add(versionDropDown);
        insertIssueForm.add(priorityDropDown);
        insertIssueForm.add(new RequiredTextField("issueName", new PropertyModel<String>(this, "issue.name")));
        insertIssueForm.add(new RequiredTextField("issueSummary", new PropertyModel<String>(this, "issue.summary")));
        insertIssueForm.add(new TextArea<String>("description", new PropertyModel<String>(this, "issue.description")));

        issueTypeList = new DropDownChoice<IssueType>("issueTypes", new PropertyModel<IssueType>(this, "issue.issueType"), issueTypeService.getIssueTypes(), new ChoiceRenderer<IssueType>("name"));
        issueTypeList.setRequired(true);
        insertIssueForm.add(issueTypeList);
        fileUploadField = new FileUploadField("fileUploadField");
        insertIssueForm.add(fileUploadField);
        insertIssueForm.setMultiPart(true);
//set a limit for uploaded file's size
        insertIssueForm.setMaxSize(Bytes.kilobytes(100));
        insertIssueForm.add(cfListView);

        add(new FeedbackPanel("feedbackPanel"));

        projectDropDown.add(
                new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(componentDropDown);
                target.add(versionDropDown);
                target.add(cfListView);
            }
        });
    }
//<editor-fold defaultstate="collapsed" desc="getter/setter">

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public Project getSelectedProject() {
        return selectedProject;
    }

    public void setSelectedProject(Project selectedProject) {
        this.selectedProject = selectedProject;
    }

    public List<Issue> getIssueList() {
        return issueList;
    }

    public void setIssueList(List<Issue> issueList) {
        this.issueList = issueList;
    }

    public List<CustomField> getProjectCustomFieldList() {
        return projectCustomFieldList;
    }

    public void setProjectCustomFieldList(List<CustomField> projectCustomFieldList) {
        this.projectCustomFieldList = projectCustomFieldList;
    }

    public List<CustomFieldIssueValue> getCfIssueValues() {
        return cfIssueValues;
    }

    public void setCfIssueValues(List<CustomFieldIssueValue> cfIssueValues) {
        this.cfIssueValues = cfIssueValues;
    }
    //</editor-fold>
}
