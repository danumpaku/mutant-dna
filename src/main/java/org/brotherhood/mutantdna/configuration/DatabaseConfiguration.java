package org.brotherhood.mutantdna.configuration;

import javax.sql.DataSource;

import org.brotherhood.mutantdna.clients.AwsSecretManagerClient;
import org.brotherhood.mutantdna.entities.DbConnectionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@PropertySource("application.properties")
public class DatabaseConfiguration {
	
	@Autowired
	private Environment env;

	@Bean
	public DataSource dataSource() {
		String profile = env.getProperty("spring.profiles.active");
		if (profile.equals("test"))
			return testDataSource();
		return awsSecretDataSource();
	}

	private DataSource awsSecretDataSource() {

		String databaseSecretName = System.getenv("DATABASE_SECRET_NAME");
		System.out.println("Reading Secret " + databaseSecretName);
		AwsSecretManagerClient secretManager = new AwsSecretManagerClient();
		DbConnectionInfo dbInfo = secretManager.getSecretAs(databaseSecretName, DbConnectionInfo.class);

		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl("jdbc:postgresql://" + dbInfo.getHost() + ":" + dbInfo.getPort() + "/" + dbInfo.getDbname());
		dataSource.setUsername(dbInfo.getUsername());
		dataSource.setPassword(dbInfo.getPassword());

		return dataSource;	    
	}

	private DataSource testDataSource() {

		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
		dataSource.setUrl(env.getProperty("jdbc.url"));
		dataSource.setUsername(env.getProperty("jdbc.user"));
		dataSource.setPassword(env.getProperty("jdbc.pass"));

		return dataSource;
	}
}
