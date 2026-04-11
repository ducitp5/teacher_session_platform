package com.teachersession.test;

import com.teachersession.database.seed.DatabaseSeeder;
import com.teachersession.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DatabaseSeederTest {

    @Autowired
    private DatabaseSeeder databaseSeeder;

    @Test
    void testSeedExecution() {

        Assertions.assertDoesNotThrow(() -> databaseSeeder.seed(), "Seeding should execute without exceptions.");
    }

    @Test
    void testSeedUsers() {

        List<User> seedUsers = databaseSeeder.seedUsers();

        System.out.println(seedUsers);
    }
}
