package org.brotherhood.mutantdna.clients;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AwsSecretManagerClient {

	public AwsSecretManagerClient() {
	}

	public String getSecret(String secretName) {
		AWSSecretsManager client  = AWSSecretsManagerClientBuilder
				.standard()
				.build();

		GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
				.withSecretId(secretName);
		
		GetSecretValueResult getSecretValueResult;

		try {
			getSecretValueResult = client.getSecretValue(getSecretValueRequest);
		} catch (Exception e) {
			System.out.println("Error reading secret " + secretName + ": \n" + e);
			throw e;
		} 
		return getSecretValueResult.getSecretString();
	}

	public <T> T getSecretAs(String secretName, Class<T> type) {
		String jsonSecret = getSecret(secretName);
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(jsonSecret, type);
		}
		catch (Exception e) {
			System.out.println("Error parsing secret " + secretName + ": \n" + e);
			return null;
		}
	}
}