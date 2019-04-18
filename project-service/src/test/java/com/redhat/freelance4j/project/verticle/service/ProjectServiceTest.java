package com.redhat.freelance4j.project.verticle.service;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class ProjectServiceTest extends MongoTestBase {

    private Vertx vertx;

    @Before
    public void setup(TestContext context) throws Exception {
        vertx = Vertx.vertx();
        vertx.exceptionHandler(context.exceptionHandler());
        JsonObject config = getConfig();
        mongoClient = MongoClient.createNonShared(vertx, config);
        Async async = context.async();
        dropCollection(mongoClient, "projects", async, context);
        async.await(10000);
    }

    @After
    public void tearDown() throws Exception {
        mongoClient.close();
        vertx.close();
    }


    @Test
    public void testGetProjects(TestContext context) throws Exception {
        Async saveAsync = context.async(2);
        String projectId1 = "111111";
        JsonObject json1 = new JsonObject()
                .put("projectId", projectId1)
                .put("ownerFirstName", "ownerFirstName1")
                .put("ownerLastName", "ownerLastName1")
                .put("ownerEmailAddress", "1@test.com")
                .put("projectTitle", "projectTitle1")
                .put("projectDescription", "projectDescription1")
                .put("projectStatus", "projectStatus1");

        mongoClient.save("projects", json1, ar -> {
            if (ar.failed()) {
                context.fail();
            }
            saveAsync.countDown();
        });

        String projectId2 = "222222";
        JsonObject json2 = new JsonObject()
        		.put("projectId", projectId2)
                .put("ownerFirstName", "ownerFirstName2")
                .put("ownerLastName", "ownerLastName2")
                .put("ownerEmailAddress", "2@test.com")
                .put("projectTitle", "projectTitle2")
                .put("projectDescription", "projectDescription2")
                .put("projectStatus", "projectStatus2");

        mongoClient.save("projects", json2, ar -> {
            if (ar.failed()) {
                context.fail();
            }
            saveAsync.countDown();
        });

        saveAsync.await();

        ProjectService service = new ProjectServiceImpl(vertx, getConfig(), mongoClient);

        Async async = context.async();

        service.getProjects(ar -> {
            if (ar.failed()) {
                context.fail(ar.cause().getMessage());
            } else {
                assertThat(ar.result(), notNullValue());
                assertThat(ar.result().size(), equalTo(2));
                Set<String> itemIds = ar.result().stream().map(p -> p.getProjectId()).collect(Collectors.toSet());
                assertThat(itemIds.size(), equalTo(2));
                assertThat(itemIds, allOf(hasItem(projectId1),hasItem(projectId2)));
                async.complete();
            }
        });
    }

    @Test
    @Ignore
    public void testGetProjectsByStatus(TestContext context) throws Exception {
        Async saveAsync = context.async(2);
        String projectId1 = "111111";
        JsonObject json1 = new JsonObject()
                .put("projectId", projectId1)
                .put("ownerFirstName", "ownerFirstName1")
                .put("ownerLastName", "ownerLastName1")
                .put("ownerEmailAddress", "1@test.com")
                .put("projectTitle", "projectTitle1")
                .put("projectDescription", "projectDescription1")
                .put("projectStatus", "testStatus");

        mongoClient.save("projects", json1, ar -> {
            if (ar.failed()) {
                context.fail();
            }
            saveAsync.countDown();
        });

        String projectId2 = "222222";
        JsonObject json2 = new JsonObject()
        		.put("projectId", projectId2)
                .put("ownerFirstName", "ownerFirstName2")
                .put("ownerLastName", "ownerLastName2")
                .put("ownerEmailAddress", "2@test.com")
                .put("projectTitle", "projectTitle2")
                .put("projectDescription", "projectDescription2")
                .put("projectStatus", "testStatus");

        mongoClient.save("projects", json2, ar -> {
            if (ar.failed()) {
                context.fail();
            }
            saveAsync.countDown();
        });

        String projectId3 = "333333";
        JsonObject json3 = new JsonObject()
        		.put("projectId", projectId3)
                .put("ownerFirstName", "ownerFirstName3")
                .put("ownerLastName", "ownerLastName3")
                .put("ownerEmailAddress", "3@test.com")
                .put("projectTitle", "projectTitle3")
                .put("projectDescription", "projectDescription3")
                .put("projectStatus", "projectStatus3");

        mongoClient.save("projects", json3, ar -> {
            if (ar.failed()) {
                context.fail();
            }
            saveAsync.countDown();
        });
        
        saveAsync.await();

        ProjectService service = new ProjectServiceImpl(vertx, getConfig(), mongoClient);

        Async async = context.async();

        service.getProjectsByStatus("testStatus", ar -> {
            if (ar.failed()) {
                context.fail(ar.cause().getMessage());
            } else {
                assertThat(ar.result(), notNullValue());
                assertThat(ar.result().size(), equalTo(2));
                Set<String> itemIds = ar.result().stream().map(p -> p.getProjectId()).collect(Collectors.toSet());
                assertThat(itemIds.size(), equalTo(2));
                assertThat(itemIds, allOf(hasItem(projectId1),hasItem(projectId2)));
                async.complete();
            }
        });
    }
    
    @Test
    public void testGetProject(TestContext context) throws Exception {
        Async saveAsync = context.async(2);
        String projectId1 = "111111";
        JsonObject json1 = new JsonObject()
                .put("projectId", projectId1)
                .put("ownerFirstName", "ownerFirstName1")
                .put("ownerLastName", "ownerLastName1")
                .put("ownerEmailAddress", "1@test.com")
                .put("projectTitle", "projectTitle1")
                .put("projectDescription", "projectDescription1")
                .put("projectStatus", "projectStatus1");

        mongoClient.save("projects", json1, ar -> {
            if (ar.failed()) {
                context.fail();
            }
            saveAsync.countDown();
        });

        String projectId2 = "222222";
        JsonObject json2 = new JsonObject()
        		.put("projectId", projectId2)
                .put("ownerFirstName", "ownerFirstName2")
                .put("ownerLastName", "ownerLastName2")
                .put("ownerEmailAddress", "2@test.com")
                .put("projectTitle", "projectTitle2")
                .put("projectDescription", "projectDescription2")
                .put("projectStatus", "projectStatus2");

        mongoClient.save("projects", json2, ar -> {
            if (ar.failed()) {
                context.fail();
            }
            saveAsync.countDown();
        });

        saveAsync.await();

        ProjectService service = new ProjectServiceImpl(vertx, getConfig(), mongoClient);

        Async async = context.async();

        service.getProject("111111", ar -> {
            if (ar.failed()) {
                context.fail(ar.cause().getMessage());
            } else {
                assertThat(ar.result(), notNullValue());
                assertThat(ar.result().getProjectId(), equalTo("111111"));
                assertThat(ar.result().getProjectTitle(), equalTo("projectTitle1"));
                async.complete();
            }
        });
    }

    @Test
    public void testGetNonExistingProject(TestContext context) throws Exception {
        Async saveAsync = context.async(1);
        String projectId1 = "111111";
        JsonObject json1 = new JsonObject()
                .put("projectId", projectId1)
                .put("ownerFirstName", "ownerFirstName1")
                .put("ownerLastName", "ownerLastName1")
                .put("ownerEmailAddress", "1@test.com")
                .put("projectTitle", "projectTitle1")
                .put("projectDescription", "projectDescription1")
                .put("projectStatus", "projectStatus1");

        mongoClient.save("projects", json1, ar -> {
            if (ar.failed()) {
                context.fail();
            }
            saveAsync.countDown();
        });

        saveAsync.await();

        ProjectService service = new ProjectServiceImpl(vertx, getConfig(), mongoClient);

        Async async = context.async();

        service.getProject("222222", ar -> {
            if (ar.failed()) {
                context.fail(ar.cause().getMessage());
            } else {
                assertThat(ar.result(), nullValue());
                async.complete();
            }
        });
    }

    @Test
    public void testPing(TestContext context) throws Exception {
        ProjectService service = new ProjectServiceImpl(vertx, getConfig(), mongoClient);

        Async async = context.async();
        service.ping(ar -> {
            assertThat(ar.succeeded(), equalTo(true));
            async.complete();
        });
    }

}
