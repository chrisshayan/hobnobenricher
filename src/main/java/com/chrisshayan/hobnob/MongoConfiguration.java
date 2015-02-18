package com.chrisshayan.hobnob;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.util.Arrays;

/**
 * Created by chrisshayan on 2/18/15.
 */
@Configuration
@PropertySource("classpath:mongodb.properties")
public class MongoConfiguration {

    @Value("${db.host}")
    private String mongoHost;

    @Value("${db.name}")
    private String mongoName;

    @Value("${db.username}")
    private String mongoUsername;

    @Value("${db.password}")
    private String mongoPassword;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        final MongoCredential mongoCredential = MongoCredential.createMongoCRCredential(
                mongoUsername, mongoName, mongoPassword.toCharArray()
        );

        return new SimpleMongoDbFactory(new MongoClient(
                new ServerAddress(mongoHost),
                Arrays.asList(mongoCredential)
        ), mongoName);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory());
    }
}
