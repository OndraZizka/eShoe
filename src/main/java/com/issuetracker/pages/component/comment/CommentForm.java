/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.component.comment;

import com.issuetracker.dao.api.CommentDao;
import com.issuetracker.dao.api.IssueDao;
import com.issuetracker.model.Comment;
import com.issuetracker.model.Issue;
import com.issuetracker.pages.CreateProject;
import com.issuetracker.pages.IssueDetail;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author mgottval
 */
public class CommentForm extends Panel {

    @Inject
    private CommentDao commentDao;
    @Inject
    private IssueDao issueDao;
    private Form<Comment> commentForm;
    private Comment comment;
    private List<Comment> comments;
    private Issue issue;

    public CommentForm(String id, final IModel<Issue> issueModel) {
        super(id);

        issue = issueModel.getObject();


        add(new FeedbackPanel("feedback"));

        commentForm = new Form<Comment>("commentForm") {
            @Override
            protected void onSubmit() {
//                comment = new Comment();
//                commentDao.insert(comment);
                try {
                    comments = issue.getComments();
                } catch (NullPointerException e) {
                    comments = new ArrayList<Comment>();
                    Logger.getLogger(CommentForm.class.getName()).log(Level.SEVERE, "Ted se to loguje - melo by jen poprve");
                }
//                comments = issueDao.getComments(issue);
                comments.add(comment);
                issue.setComments(comments);
                issueDao.updateIssue(issue);
//                comment = new Comment();
                String s = "";
                for (Comment comment1 : issue.getComments()) {
                    s = s + comment1.getContent();
                }
                Logger.getLogger(CommentForm.class.getName()).log(Level.SEVERE, s);
                comment = new Comment();
            }
        };
        add(commentForm);

        commentForm.add(new TextArea<String>("content", new PropertyModel<String>(this, "comment.content")));

    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }
}
