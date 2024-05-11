package com.dataslab.vscan;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.spock.Testcontainers;

import java.lang.annotation.*;

/**
 * This annotation marks integration tests dao level
 * Such tests should only touch dao classes
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited

@SpringBootTest
@ActiveProfiles({"it"})
@Testcontainers
@ContextConfiguration(initializers = {TestContainerConfig.Initializer.class})
public @interface IntegrationTest {
}
