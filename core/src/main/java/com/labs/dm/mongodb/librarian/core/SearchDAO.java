/*
 * Copyright daniel.mroczka@gmail.com. All rights reserved. 
 */
package com.labs.dm.mongodb.librarian.core;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author daniel
 */
public class SearchDAO {

    private final DB db;

    public SearchDAO() throws UnknownHostException {
        MongoClient client = new MongoClient(new MongoClientURI("mongodb://wmb:wmb123@paulo.mongohq.com:10027/books"));
        db = client.getDB("books");
    }

    public List<Book> find(String title, String ext) {
        List<Book> list = new ArrayList<>();
        DBCollection col = db.getCollection("ebook");
        DBObject query = new BasicDBObject();
        query.put("name", Pattern.compile(title));
        try (DBCursor res = col.find(query)) {
            while (res.hasNext()) {
                DBObject item = res.next();
                list.add(new Book((String) item.get("name")));
                System.out.println("Add" + item);
                
            }
        }
        return list;
    }

}
