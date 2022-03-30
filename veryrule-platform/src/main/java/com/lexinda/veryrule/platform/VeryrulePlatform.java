package com.lexinda.veryrule.platform;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 
 * @author lexinda
 *
 */
@EnableTransactionManagement
@SpringBootApplication
@MapperScan("com.lexinda.veryrule.platform.mapper")
public class VeryrulePlatform 
{
	
	private static final Logger logger = LoggerFactory.getLogger(VeryrulePlatform.class);
	 
    public static void main(String[] args) {
        SpringApplication.run(VeryrulePlatform.class, args);
    }
}
