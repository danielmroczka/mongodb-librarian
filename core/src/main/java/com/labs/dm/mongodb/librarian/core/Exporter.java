/*
 * Copyright daniel.mroczka@gmail.com. All rights reserved. 
 */
package com.labs.dm.mongodb.librarian.core;

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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 * @author daniel
 */
public class Exporter {

    private static final Logger logger = Logger.getLogger("Exporter");
    private final String dir;
    private static final String mongoDB = "books";
    private final DB db;

    enum Mode {

        //Removes all documents and inserts 
        DELETE_ADD,
        //Adds only new documents
        ADD,
        //Adds new documents, remove unexistsing
        ADD_REMOVE_ORPHANS
    };

    public Exporter(String mongoUri, String dir) throws UnknownHostException {
        MongoClient client = new MongoClient(new MongoClientURI(mongoUri));
        this.dir = dir;
        db = client.getDB(mongoDB);
    }

    public static void main(String[] args) throws IOException {
        Exporter exporter = new Exporter("mongodb://wmb:wmb123@paulo.mongohq.com:10027/books", "D:\\dummy");
        exporter.execute();
    }

    private void execute() throws IOException {
        Collection<File> ebookList = FileUtils.listFiles(new File(dir), new String[]{"pdf", "epub", "mobi"}, true);
        export(ebookList, db.getCollection("ebook"), Mode.ADD_REMOVE_ORPHANS);
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
     * @param mode
     */
    private void export(Collection<File> list, DBCollection collection, Mode mode) throws FileNotFoundException, IOException {
        int deleted = 0;
        int inserted = 0;
        int updated = 0;
        int counter = 0;
        if (mode == Mode.DELETE_ADD) {
            collection.remove(new BasicDBObject());
        }
        Set<String> set = new HashSet<String>();
        for (File file : list) {
            counter++;
            DBObject item = new BasicDBObject();
            PdfReader reader = null;
            String md5 = DigestUtils.md5Hex(new FileInputStream(file));
            item.put("_id", md5);
            set.add(md5);
            updateObject(item, file);
            try {
                reader = new PdfReader(file.getPath());
                updateObject(item, reader);
                logger.info(counter + "/" + list.size() + " " + reader.getInfo());
            } catch (IOException e) {
                logger.severe(e.getMessage());
            } finally {
                if (reader != null) {
                    reader.close();
                }

            }
            logger.info(file.getPath());
            BasicDBObject query = new BasicDBObject("_id", item.get("_id"));
            if (mode == Mode.DELETE_ADD || collection.find(query).size() == 0) {
                collection.insert(item);
                inserted++;
                logger.fine("insert");
            } else {
                collection.update(query, item);
                updated++;
                logger.fine("update");
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
        logger.info(report.toString());
    }

    private static int removeOrphans(Set<String> set, DBCollection collection) {
        int res = 0;
        DBCursor c = collection.find();
        Set<String> all = new HashSet<String>();
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
