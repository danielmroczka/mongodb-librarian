/*
 * Copyright daniel.mroczka@gmail.com. All rights reserved. 
 */

package com.labs.dm.mongodb.librarian.core;

import java.io.Serializable;

/**
 * @author daniel
 */
public class Book implements Serializable {

    public Book(String title) {
        this.title = title;
    }

    public Book() {
    }
    
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    
}
