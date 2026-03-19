package com.teachersession.controllers;

import com.teachersession.database.seed.DatabaseSeeder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/seeder")
@RequiredArgsConstructor
public class SeederController {

    private final DatabaseSeeder databaseSeeder;

    @GetMapping("/run")
    public ResponseEntity<?> runSeeder() {

        Map<String, Object> result = databaseSeeder.seed();

        return ResponseEntity.ok(result);
    }
}
