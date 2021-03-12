package org.brotherhood.mutantdna.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DbConnectionInfo {
	
	@Getter
	private String host;
	
	@Getter
	private int port;
	
	@Getter
	private String dbname;
	
	@Getter
	private String username;
	
	@Getter
	private String password;
}
