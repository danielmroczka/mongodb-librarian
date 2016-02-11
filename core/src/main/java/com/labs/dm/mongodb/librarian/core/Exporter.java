/*
 * Copyright daniel.mroczka@gmail.com. All rights reserved. 
 */
package com.labs.dm.mongodb.librarian.core;

import com.itextpdf.text.pdf.PdfReader;
import com.mongodb.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * @author daniel
 */
public class Exporter {

    private static final Logger logger = Logger.getLogger("Exporter");
    private final String dir;
    private static final String mongoDB = "books";
    private static final String mongoCollection = "ebook";
    private final DB db;

    enum Mode {

        /**
         * Removes all documents and inserts
         */
        DELETE_ADD,
        /**
         * Adds new elements and updates other.
         */
        ADD_UPDATE,
        /**
         * Adds only new documents
         */
        ADD,
        /**
         * Adds new documents, remove nonexistent
         */
        ADD_REMOVE_ORPHANS
    }

    public Exporter(String mongoUri, String dir) throws UnknownHostException {
        MongoClient client = new MongoClient(new MongoClientURI(mongoUri));
        this.dir = dir;
        db = client.getDB(mongoDB);
    }

    public static void main(String[] args) throws IOException {
        Exporter exporter = new Exporter("mongodb://wmb:wmb123@ds062178.mongolab.com:62178/books", "D:\\pdf");
        exporter.execute();
    }

    private void execute() throws IOException {
        Collection<File> ebookList = FileUtils.listFiles(new File(dir), new String[]{"pdf", "epub", "mobi"}, true);
        export(ebookList, db.getCollection(mongoCollection), Mode.ADD_REMOVE_ORPHANS);
    }

    private void updateObject(DBObject item, File file) {
        item.put("name", FilenameUtils.removeExtension(file.getName()));
        item.put("ext", FilenameUtils.getExtension(file.getName()));
        item.put("path", file.getPath());
        item.put("size", file.length());

    }

    private void updateObject(DBObject item, PdfReader reader) {
        item.put("pages", reader.getNumberOfPages());
        Map<String, String> map = new ConcurrentHashMap<String, String>();
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

        insertNotEmpty(item, map, "Title");
        insertNotEmpty(item, map, "Author");

        item.put("info", map);

    }

    private void insertNotEmpty(DBObject item, Map<String, String> map, String key) {
        if (map.containsKey(key) && !map.get(key).isEmpty()) {
            item.put(key.toLowerCase(), map.get(key));
            map.remove(key);
        }
        if (!key.equals(key.toLowerCase())) {
            insertNotEmpty(item, map, key.toLowerCase());
        }
    }

    /**
     *
     * @param list
     * @param collection
     * @param mode
     */
    private void export(Collection<File> list, DBCollection collection, Mode mode) throws IOException {
        int deleted = 0, inserted = 0, updated = 0, failed = 0, counter = 0;
        if (mode == Mode.DELETE_ADD) {
            collection.remove(new BasicDBObject());
        }
        Set<String> set = new HashSet<>();
        for (File file : list) {
            counter++;
            DBObject item = new BasicDBObject();
            String md5 = DigestUtils.md5Hex(new FileInputStream(file));
            item.put("_id", md5);
            set.add(md5);
            updateObject(item, file);
            if ("pdf".equalsIgnoreCase(FilenameUtils.getExtension(file.getName()))) {
                PdfReader reader = null;
                try {
                    reader = new PdfReader(file.getPath());
                    updateObject(item, reader);

                } catch (IOException e) {
                    logger.severe(e.getMessage());
                } finally {
                    if (reader != null) {
                        reader.close();
                    }
                }
            }
            logger.info(counter + "/" + list.size() + " " + file.getName());
            BasicDBObject query = new BasicDBObject("_id", item.get("_id"));
            try {
                if (mode == Mode.DELETE_ADD || collection.find(query).size() == 0) {
                    collection.insert(item);
                    inserted++;
                } else {
                    collection.update(query, item);
                    updated++;
                }
            } catch (Exception e) {
                logger.severe(e.getMessage());
                failed++;
            }

        }
        if (mode == Mode.ADD_REMOVE_ORPHANS) {
            deleted = removeOrphans(set, collection);
        }

        StringBuilder report = new StringBuilder();
        report.append("Summary report").append("\n");
        report.append("Inserted items: ").append(inserted).append("\n");
        report.append("Updated items: ").append(updated).append("\n");
        report.append("Removed items: ").append(deleted).append("\n");
        report.append("Failed updates: ").append(failed).append("\n");
        report.append("Summary files: ").append(counter);
        logger.info(report.toString());
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
