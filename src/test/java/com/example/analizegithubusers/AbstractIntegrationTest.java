package com.example.analizegithubusers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

import java.util.stream.Stream;

@Slf4j
@SpringBootTest(classes = {AnalizeGithubUsersApplication.class})
@ActiveProfiles(profiles = "test")
@Testcontainers
public abstract class AbstractIntegrationTest {

    private static final String mongoDockerImageName = "mongo";
    private static final String mongoDockerImageVersion = "4.0";

    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer(getMongoDockerImage())
            .withReuse(true);

    static {
        Startables.deepStart(Stream.of(mongoDBContainer)).join();
    }

    private static DockerImageName getMongoDockerImage() {
        String name = mongoDockerImageName + ":" + mongoDockerImageVersion;
        return DockerImageName.parse(name).asCompatibleSubstituteFor("mongo");
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            String endpointUri = mongoDBContainer.getReplicaSetUrl();
            TestPropertyValues.of(
                    "spring.data.mongodb.uri=" + endpointUri
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
