package com.issuetracker.dao.api;

import com.issuetracker.model.Component;

import java.util.List;

/**
 *
 * @author mgottval
 */
public interface ComponentDao {
    
    void insert(Component component);
    
    List<Component> getComponents();
    
    void remove(Component component);
}
