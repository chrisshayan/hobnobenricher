package com.chrisshayan.hobnob;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import static org.junit.Assert.assertNotNull;

@PropertySource("classpath:mongodb.properties")
public class MongoConfigurationTest {
    @Test
    public void loadContext() throws Exception {
        final ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MongoConfiguration.class);
        final MongoOperations mongoOperations = (MongoOperations) applicationContext.getBean("mongoTemplate");

        assertNotNull(mongoOperations);

        final Query countEmptyPictureUsersQuery = new Query(Criteria.where("pictureUrl").is(""));
        final long count = mongoOperations.count(countEmptyPictureUsersQuery, UserModel.class);

        System.out.println("count = " + count);
        assertNotNull(count);
    }
}