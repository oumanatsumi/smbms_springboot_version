package com.ouma.smbms_springboot_version;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@ComponentScan("com.ouma")
@SpringBootApplication
public class SmbmsSpringbootVersionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmbmsSpringbootVersionApplication.class, args);
    }

}
