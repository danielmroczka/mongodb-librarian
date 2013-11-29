/*
 * Copyright daniel.mroczka@gmail.com. All rights reserved. 
 */
package com.labs.dm.mongodb.librarian.exporter;

import com.itextpdf.text.pdf.PdfReader;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

/**
 * @author daniel
 */
public class Exporter {

    private final static String DIR = "D:\\dummy";

    public static void main(String[] args) throws IOException {
        MongoClientURI uri = new MongoClientURI("mongodb://wmb:wmb123@paulo.mongohq.com:10027/books");
        MongoClient client = new MongoClient(uri);
        DB db = client.getDB("books");
        //DBCollection collection = db.getCollection("pdf3");

        Collection<File> pdfList = FileUtils.listFiles(new File(DIR), new String[]{"pdf"}, true);
        Collection<File> mobiList = FileUtils.listFiles(new File(DIR), new String[]{"mobi"}, true);
        Collection<File> epubList = FileUtils.listFiles(new File(DIR), new String[]{"epub"}, true);
        System.out.println(pdfList.size());
        System.out.println(epubList.size());
        System.out.println(mobiList.size());
        export(pdfList, "pdf", db.getCollection("pdf8"), 2);
        //export(epubList, "epub", db.getCollection("epub"), 1);
        //export(mobiList, "mobi", db.getCollection("mobi"), 1);
    }

    private static void updateObject(DBObject item, File file, String ext) {
        item.put("name", file.getName().substring(0, file.getName().indexOf("." + ext)));
        item.put("ext", ext);
        item.put("path", file.getPath());
        item.put("size", file.length());

    }

    private static void updateObject(DBObject item, PdfReader reader) {
        item.put("pages", reader.getNumberOfPages());
        Map<String, String> map = new ConcurrentHashMap<>();
        map.putAll(reader.getInfo());

        Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            if (entry.getKey().contains(".")) {
                String newKey = entry.getKey().replaceAll("\\.", "_").trim();
                map.put(newKey, entry.getValue());
                map.remove(entry.getKey());
            }
        }

        if (map.containsKey("Title")) {
            item.put("title", map.get("Title"));
            map.remove("Title");
        }
        if (map.containsKey("title")) {
            item.put("title", map.get("title"));
            map.remove("title");
        }
        if (map.containsKey("Author")) {
            item.put("author", map.get("Author"));
            map.remove("Author");
        }

        item.put("info", map);

    }

    /**
     *
     * @param list
     * @param ext
     * @param collection
     * @param mode 0-delete and insert, 1-update, 2-update and delete orphan
     */
    public static void export(Collection<File> list, String ext, DBCollection collection, int mode) {
        int deleted = 0;
        int inserted = 0;
        int updated = 0;
        int counter = 0;
        if (mode == 0) {
            collection.remove(new BasicDBObject());
        }
        Set<String> set = new HashSet<>();
        for (File file : list) {
            counter++;
            DBObject item = new BasicDBObject();
            PdfReader reader = null;
            try {
                String md5 = DigestUtils.md5Hex(new FileInputStream(file));
                item.put("_id", md5);
                set.add(md5);
                updateObject(item, file, ext);
                reader = new PdfReader(file.getPath());
                System.out.println(counter + "/" + list.size() + " " + reader.getInfo());
                System.out.println(file.getPath());
                updateObject(item, reader);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    BasicDBObject query = new BasicDBObject("_id", item.get("_id"));
                    if (mode == 0 || collection.find(query).size() == 0) {
                        collection.insert(item);
                        System.out.println("insert");
                        inserted++;
                    } else {
                        updated++;
                        System.out.println("update");
                        collection.update(item, item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (reader != null) {
                    reader.close();
                }
            }

        }
        if (mode == 2) {
            deleted = removeOrphans(set, collection);
        }
        System.out.println("-------------------------------------------------");
        System.out.println("Inserted items: " + inserted);
        System.out.println("Updated items: " + updated);
        System.out.println("Removed items: " + deleted);
    }

    private static int removeOrphans(Set<String> set, DBCollection collection) {
        int res = 0;
        DBCursor c = collection.find();
        Set<String> all = new HashSet<>();
        while (c.hasNext()) {
            DBObject ob = c.next();
            all.add(ob.get("_id").toString());
        }
        all.removeAll(set);
        for (String key : all) {
            BasicDBObject query = new BasicDBObject("_id", key);
            collection.remove(query);
            res++;
        }
        return res;
    }
}
