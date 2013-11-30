/*
 * Copyright daniel.mroczka@gmail.com. All rights reserved. 
 */

package com.labs.dm.mongodb.librarian.mongo;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteConcern;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author daniel
 */
@Configuration
@ComponentScan(basePackageClasses = ApplicationConfig.class)
@EnableMongoRepositories
public class ApplicationConfig extends AbstractMongoConfiguration {

    @Bean
    @Override
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongo(), getDatabaseName(), getUserCredentials());
        return mongoTemplate;
    }

    @Bean
    @Override
    public UserCredentials getUserCredentials() {
        return new UserCredentials("wmb", "wmb123");
    }
    
    /*
     * Factory bean that creates the Mongo instance
     */
   @Bean
    public MongoFactoryBean factory() throws Exception {
        return new MongoFactoryBean();
    }

    /*
     * Use this post processor to translate any MongoExceptions thrown in @Repository annotated classes
     */
    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
    

    @Override
    protected String getDatabaseName() {
        return "books";
    }

    @Override
    public Mongo mongo() throws Exception {
        MongoClient client = new MongoClient(new MongoClientURI("mongodb://wmb:wmb123@paulo.mongohq.com:10027/books"));
        client.setWriteConcern(WriteConcern.SAFE);
        return client;
    }
}

