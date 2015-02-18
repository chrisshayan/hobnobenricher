package com.chrisshayan.hobnob;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created by chrisshayan on 2/18/15.
 */
public class EnricherTest {

    @Test
    public void enrich() throws Exception {
        final ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MongoConfiguration.class);
        final MongoOperations mongoOperations = (MongoOperations) applicationContext.getBean("mongoTemplate");

        assertNotNull(mongoOperations);

        final Query emptyPictureUsersQuery = new Query(Criteria.where("pictureUrl").is(""));
        final List<UserModel> userModels = mongoOperations.find(emptyPictureUsersQuery, UserModel.class);
        for (UserModel userModel : userModels) {
            GravatarService gravatarService = new GravatarService();
            final GravatarModel gravatarProfile = gravatarService.findGravatarProfile(userModel.getEmail());

            if (gravatarProfile != null) {
                final Query updateQuery = new Query(Criteria.where("email").is(userModel.getEmail()));
                final Update update = new Update();
                update.set("pictureUrl", gravatarProfile.getThumbnailUrl());
                mongoOperations.updateFirst(updateQuery, update, UserModel.class);
                mongoOperations.updateFirst(updateQuery, update, UserModel.class);

                System.out.println("userModel = " + userModel.getEmail());
                System.out.println("gravatarProfile = " + gravatarProfile.getThumbnailUrl());
            }
            Thread.sleep(4000);
        }
        final long count = mongoOperations.count(emptyPictureUsersQuery, UserModel.class);
        System.out.println("count = " + count);
    }
}
