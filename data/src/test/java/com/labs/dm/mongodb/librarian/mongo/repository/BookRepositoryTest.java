/*
 * Copyright daniel.mroczka@gmail.com. All rights reserved. 
 */
package com.labs.dm.mongodb.librarian.mongo.repository;

import com.labs.dm.mongodb.librarian.mongo.ApplicationConfig;
import com.labs.dm.mongodb.librarian.mongo.domain.Book;
import java.util.Collection;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author daniel
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
public class BookRepositoryTest {

    @Autowired
    private BookRepository repository;

    @Test
    public void insertThanFindAll() {
        //GIVEN
        //Book book = new Book("1984", new Author("George", "Orwell"));
        //WHEN
        //repository.save(book);
        Book read = repository.findOne("996101444c5c0c214cb8046f0e828999");
        //THEN
        assertNotNull(read);
    }
    
    //@Test
    public void findByTitle() {
        //GIVEN
        //Book book = new Book("1984", new Author("George", "Orwell"));
        //WHEN
        //repository.save(book);
        Collection<Book> read = repository.findByTitle("Java");
        //THEN
        assertTrue(read.size() > 0);
    }    
}
