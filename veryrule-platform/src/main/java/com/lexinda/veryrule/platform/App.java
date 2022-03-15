package com.lexinda.veryrule.platform;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Hello world!
 *
 */
@EnableTransactionManagement
@SpringBootApplication
@MapperScan("com.lexinda.veryrule.platform.mapper")
public class App 
{
	
	private static final Logger logger = LoggerFactory.getLogger(App.class);
	 
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
