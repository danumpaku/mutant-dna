package org.brotherhood.mutantdna.entities;

import lombok.Getter;

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
