package com.ouma.smbms_springboot_version;

import com.ouma.filter.CharacterEncodingFilter;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@ComponentScan("com.ouma")
@MapperScan("com.ouma.mapper")
@ServletComponentScan("com.ouma.filter")
@SpringBootApplication
public class SmbmsSpringbootVersionApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SmbmsSpringbootVersionApplication.class, args);
    }

}
