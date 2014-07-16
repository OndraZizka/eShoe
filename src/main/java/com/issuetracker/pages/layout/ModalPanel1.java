package com.issuetracker.pages.layout;

import com.issuetracker.model.Issue;
import com.issuetracker.model.User;
import com.issuetracker.service.api.IssueService;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author mgottval
 */
public class ModalPanel1 extends Panel {
    private ListView<User> watchersListView;
    private List<User> watchersList;
    
    @Inject
    private IssueService issueService;
    
    private Issue issue;

    /**
     * @param id
     */
    public ModalPanel1(String id, IModel<List<User>> watchersModel)
    {
        super(id); 
        this.watchersList = watchersModel.getObject();

        if(watchersList == null) {
            watchersList = new ArrayList<User>();
        }

        add(new Label("name", "LABEL"));
        watchersListView = new ListView<User>("watchersListView", new PropertyModel<List<User>>(this, "watchersList")) {
            @Override
            protected void populateItem(ListItem<User> item) {
                final User user = item.getModelObject();
                item.add(new Label("name", user.getUsername()));
            }
        };
//        watchersListView.setOutputMarkupId(true);
        add(watchersListView);
    }

    
    
    public List<User> getWatchersList() {
        return watchersList;
    }

    public void setWatchersList(List<User> watchersList) {
        this.watchersList = watchersList;
    }
    
    
    
}
