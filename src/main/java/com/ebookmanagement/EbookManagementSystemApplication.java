package com.ebookmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application entry point.
 * Running this class boots the embedded Tomcat server and the whole app.
 */
@SpringBootApplication
public class EbookManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbookManagementSystemApplication.class, args);
    }
}
