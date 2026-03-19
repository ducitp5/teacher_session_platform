package com.teachersession.test;

import com.teachersession.database.seed.DatabaseSeeder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DatabaseSeederTest {

    @Autowired
    private DatabaseSeeder databaseSeeder;

    @Test
    void testSeedExecution() {

        Assertions.assertDoesNotThrow(() -> databaseSeeder.seed(), "Seeding should execute without exceptions.");
    }
}
