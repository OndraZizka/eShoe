/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.model;


import com.issuetracker.TEST.Status;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

/**
 *
 * @author mgottval
 */
@Entity
public class Issue implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long issueId;
    @Column(unique=true)
    private String name;
    private String description;
    @ManyToOne(cascade = CascadeType.MERGE)
    private IssueType issueType;
    private Priority priority;
//    @ManyToOne
//    private Status status;
    private Status status; 
    @ManyToOne
    private Resolution resolution;
    @ManyToOne
    private User creator;
    @ManyToOne
    private User owner;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Project project;
    
    @ManyToMany
    List<User> watches;
    
    @ManyToMany
    List<User> votes;
    
    @ManyToMany
    List<Component> components;
    

    //<editor-fold defaultstate="collapsed" desc="getter/setter">
    public Long getIssueId() {
        return issueId;
    }
    
    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public IssueType getissueType() {
        return issueType;
    }
    
    public void setissueType(IssueType issueType) {
        this.issueType = issueType;
    }
    
    public Priority getPriority() {
        return priority;
    }
    
    public void setPriority(Priority priority) {
        this.priority = priority;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public Resolution getResolution() {
        return resolution;
    }
    
    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }
    
    public User getCreator() {
        return creator;
    }
    
    public void setCreator(User creator) {
        this.creator = creator;
    }
    
    public User getOwner() {
        return owner;
    }
    
    public void setOwner(User owner) {
        this.owner = owner;
    }
    
    public Project getProject() {
        return project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }
    
    public List<User> getWatches() {
        return watches;
    }
    
    public void setWatches(List<User> watches) {
        this.watches = watches;
    }
    
    public List<User> getVotes() {
        return votes;
    }
    
    public void setVotes(List<User> votes) {
        this.votes = votes;
    }
    
    public List<Component> getComponents() {
        return components;
    }
    
    public void setComponents(List<Component> components) {
        this.components = components;
    }
    //</editor-fold>


    public enum Priority {
        HIGH,
        MEDIUM_HIGH,
        MEDIUM,
        MEDIUM_LOW,
        LOW;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (issueId != null ? issueId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Issue)) {
            return false;
        }
        Issue other = (Issue) object;
        if ((this.issueId == null && other.issueId != null) || (this.issueId != null && !this.issueId.equals(other.issueId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.issuetracker.Issue[ id=" + issueId + " ]";
    }
    
}
