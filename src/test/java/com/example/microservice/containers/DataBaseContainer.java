package com.example.microservice.containers;

import org.testcontainers.containers.PostgreSQLContainer;

import static java.util.Objects.isNull;

public class DataBaseContainer extends PostgreSQLContainer<DataBaseContainer> {

    private static final String IMAGE_VERSION = "postgres:12.7";
    private static final String DB_URL = "DB_URL";
    private static final String DB_USER = "DB_USER";
    private static final String DB_PASSWORD = "DB_PASSWORD";
    private static final String DB_DRIVER = "DB_DRIVER";

    private static DataBaseContainer INSTANT;

    private DataBaseContainer() {
        super(IMAGE_VERSION);
    }

    public static DataBaseContainer getInstance() {
        if (isNull(INSTANT)) {
            INSTANT = new DataBaseContainer();
        }
        return INSTANT;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty(DB_URL, INSTANT.getJdbcUrl());
        System.setProperty(DB_USER, INSTANT.getUsername());
        System.setProperty(DB_PASSWORD, INSTANT.getPassword());
        System.setProperty(DB_DRIVER, INSTANT.getDriverClassName());
    }

    @Override
    public void stop() {
        super.stop();
    }
}

