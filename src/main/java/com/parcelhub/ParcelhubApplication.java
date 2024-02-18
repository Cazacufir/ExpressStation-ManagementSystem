package com.parcelhub;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@MapperScan("com.parcelhub.mapper")
public class ParcelhubApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParcelhubApplication.class, args);
	}

}
