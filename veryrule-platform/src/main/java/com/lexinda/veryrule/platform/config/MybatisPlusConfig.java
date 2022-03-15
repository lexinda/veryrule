package com.lexinda.veryrule.platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

/**
* @author zhumengle
* @version 创建时间：2022-3-1 15:49:59
* 类说明
*/
@EnableTransactionManagement
@Configuration
public class MybatisPlusConfig {
	@Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		PaginationInnerInterceptor pageInterceptor = new PaginationInnerInterceptor();
		pageInterceptor.setOverflow(false);
		pageInterceptor.setMaxLimit(1000L);
		pageInterceptor.setDbType(DbType.MYSQL);
		interceptor.addInnerInterceptor(pageInterceptor);
		return interceptor;
    }
}
