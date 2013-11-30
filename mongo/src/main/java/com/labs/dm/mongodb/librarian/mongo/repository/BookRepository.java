package com.labs.dm.mongodb.librarian.mongo.repository;

import com.labs.dm.mongodb.librarian.mongo.domain.Book;
import org.springframework.data.repository.Repository;

/**
 *
 * @author daniel
 */
public interface BookRepository extends Repository<Book, String> {
    Book save(Book book);
    
    Book findOne(String id);
}
