package com.example.agency.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@TestConfiguration
public class TestConfig {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void initDB() throws SQLException {
        try (Connection con = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(con, new ClassPathResource("/init_type_object.sql"));
            ScriptUtils.executeSqlScript(con, new ClassPathResource("/init_type_move.sql"));
            ScriptUtils.executeSqlScript(con, new ClassPathResource("/init_object.sql"));
            ScriptUtils.executeSqlScript(con, new ClassPathResource("/init_users.sql"));
        }
    }
}