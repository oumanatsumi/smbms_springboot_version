package com.ouma.smbms_springboot_version;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@ComponentScan("com.ouma")
@MapperScan("com.ouma.mapper")
@SpringBootApplication
public class SmbmsSpringbootVersionApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SmbmsSpringbootVersionApplication.class, args);
    }

}
