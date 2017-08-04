package de.schauderhaft.storedprocedure;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages={"com.sprint.sso"})
@ComponentScan(basePackages={"com.sprint.sso"})
public class AppConfig {

	@Autowired
	private DataSource dataSource;
	
	/*@Bean
	public DriverManagerDataSource dataSource(){
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName("oracle.jdbc.OracleDriver");
		ds.setUrl("jdbc:oracle:thin:@dreaa658.dev.sprint.com:1525:TELD201");
		ds.setUsername("rms");
		ds.setPassword("acdc123!");
		return ds;
	}*/
	
	@Bean
	public JdbcTemplate jdbcTemplate(){
		JdbcTemplate jts = new JdbcTemplate();
		jts.setDataSource(dataSource);
		return jts;
	}
	
	
	/*@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
		LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		localContainerEntityManagerFactoryBean.setDataSource(dataSource());
		localContainerEntityManagerFactoryBean.setPackagesToScan("com.sprint.sso");

		EclipseLinkJpaVendorAdapter adapter = new EclipseLinkJpaVendorAdapter();
		adapter.setDatabase(Database.ORACLE);
		adapter.setShowSql(true);
		adapter.getJpaPropertyMap().put("eclipselink.weaving", false);
		//adapter.setDatabasePlatform("org.hibernate.dialect.Oracle10gDialect");

		localContainerEntityManagerFactoryBean.setJpaVendorAdapter(adapter);
		
		return localContainerEntityManagerFactoryBean;
	}*/
	
	
	@Bean
	public EntityManagerFactoryBuilder entityManagerFactoryBuilder(
			ObjectProvider<PersistenceUnitManager> persistenceUnitManager) {
		
		EclipseLinkJpaVendorAdapter jpaVendorAdapter = new EclipseLinkJpaVendorAdapter();
		jpaVendorAdapter.setDatabase(Database.ORACLE);
		jpaVendorAdapter.setShowSql(true);
		
		EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(
				jpaVendorAdapter, jpaVendorAdapter.getJpaPropertyMap(),
				persistenceUnitManager.getIfAvailable());
		builder.setCallback(null);
		return builder;
	}

	@Bean
	@Primary
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
			EntityManagerFactoryBuilder factoryBuilder) {
		Map<String, Object> vendorProperties = new HashMap<>();
		vendorProperties.put("eclipselink.weaving", "false");
		return factoryBuilder.dataSource(dataSource).packages("de.schauderhaft.storedprocedure")
				.properties(vendorProperties).build();
	}
	
	
	/*@Bean
	public JpaTransactionManager transactionManager(){
		JpaTransactionManager txm = new JpaTransactionManager();
		txm.setEntityManagerFactory(entityManagerFactory().getObject());
		return txm;
	}*/
	
	
	
}
