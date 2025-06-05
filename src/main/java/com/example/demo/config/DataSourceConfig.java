//package com.example.demo.config;
//
//import com.zaxxer.hikari.HikariDataSource;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.apache.ibatis.session.SqlSessionFactory;
//
//import javax.sql.DataSource;
//
//@Profile("!prod")
////@Configuration
////@EnableTransactionManagement
//public class DataSourceConfig {
//
//    Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Value("$spring.dbms")
//    private String dbms;
//
//    @Bean(destroyMethod = "close")
//	@ConfigurationProperties(prefix = "spring.datasource.hikari")
//	public DataSource dataSource() {
//		return new HikariDataSource();
//	}
//
//	@Bean
//	public SqlSessionFactory sqlSessionFactory(DataSource dataSource, ApplicationContext applicationContext) throws Exception {
//		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//		sqlSessionFactoryBean.setDataSource(dataSource);
//		sqlSessionFactoryBean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis/mybatis-config.xml"));
//		sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/sqlMap/"+dbms+"/**/*.xml"));
//		sqlSessionFactoryBean.setTypeAliasesPackage("com.tigen.*.dto");
//
//		return sqlSessionFactoryBean.getObject();
//	}
//
//	@Bean
//	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
//		return new SqlSessionTemplate(sqlSessionFactory);
//	}
//
//	@Bean
//	public DataSourceTransactionManager transactionManager() {
//		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource());
//		return transactionManager;
//	}
//}
