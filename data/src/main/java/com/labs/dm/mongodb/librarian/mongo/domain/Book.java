/*
 * Copyright daniel.mroczka@gmail.com. All rights reserved. 
 */
package com.labs.dm.mongodb.librarian.mongo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author daniel
 */
@Document(collection = "pdf7")
public class Book {

    @Id
    private String id;

    private String title;
    
    private String name;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
