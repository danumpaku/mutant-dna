package org.brotherhood.mutantdna.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.IOException;
import java.util.stream.Stream;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.brotherhood.mutantdna.LambdaHandler;
import org.brotherhood.mutantdna.entities.DnaRequest;
import org.brotherhood.mutantdna.entities.Stats;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized.Parameters;

import com.amazonaws.serverless.proxy.internal.testutils.AwsProxyRequestBuilder;
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LambdaHandlerTest {

	private static LambdaHandler handler;
	private static Context lambdaContext;

	@BeforeAll
	public static void setUp() {
		handler = new LambdaHandler();
		lambdaContext = new MockLambdaContext();
	}

	@Parameters
	public static Stream<Arguments> mutantsData() {
		return Stream.of(
				arguments(new DnaRequest(new String[] {
						"ATGA",
						"CAGTGC",
						"TTATGT",
						"AG",
						"CCCCTA",
						"TCACT"
				}), Status.BAD_REQUEST),
				arguments(new DnaRequest(new String[] {
						"ATGCGA",
						"CAGTGC",
						"TTATGT",
						"AGAACG",
						"CGCCTA",
						"TCACTG"
				}), Status.FORBIDDEN),
				arguments(new DnaRequest(new String[] {
						"CAGTGC",
						"ATGCGA",
						"AGAACG",
						"TCATGT",
						"CGGCTA",
						"TCACTG"
				}), Status.FORBIDDEN),
				arguments(new DnaRequest(new String[] {
						"CGCCTA",
						"ATGCGA",
						"TTATGT",
						"AGACCG",
						"CAGTGC",
						"TCACTG"
				}), Status.FORBIDDEN),
				arguments(new DnaRequest(new String[] {
						"AGAACG",
						"CAGTGC",
						"CGCCTA",
						"ATGCGA",
						"TTATGT",
						"TCACTG"
				}), Status.FORBIDDEN),
				arguments(new DnaRequest(new String[] {
						"AAAACG",
						"CAGTGC",
						"TTATGT",
						"AGAAGG",
						"TACGCC",
						"TCACTG"
				}), Status.OK), //Mixed
				arguments(new DnaRequest(new String[] {
						"AAAACG",
						"CAGTGC",
						"TTATGT",
						"AGAAGG",
						"TACCCC",
						"TCACTG"
				}), Status.OK),	//Horizontal
				arguments(new DnaRequest(new String[] {
						"CGCCTA",
						"TGACTG",
						"AGGCGA",
						"CGGTGC",
						"TGATGT",
						"AGAAGG"
				}), Status.OK),	//Vertical	
				arguments(new DnaRequest(new String[] {
						"CGCCTA",
						"TCACCG",
						"AGGCGC",
						"CAGGCC",
						"TGTTGT",
						"AGAAGG"
				}), Status.FORBIDDEN),	//Vertical
				arguments(new DnaRequest(new String[] {
						"CGCCTA",
						"TCACCG",
						"AGGCCC",
						"CAGGCC",
						"TGTTGT",
						"AGAAGG"
				}), Status.OK),	//Diagonal SE Upper
				arguments(new DnaRequest(new String[] {
						"CGCCTA",
						"TCACTG",
						"ATGCAC",
						"CATGCC",
						"TGATCT",
						"AGAAGG"
				}), Status.OK), //Diagonal SE Lower
				arguments(new DnaRequest(new String[] {
						"TCACTC",
						"TAATCT",
						"AGCCTA",
						"CACGCC",
						"ATGCAC",
						"AGAAGG"
				}), Status.FORBIDDEN),	//Diagonal NE Upper
				arguments(new DnaRequest(new String[] {
						"TCACTC",
						"TACTCT",
						"ACCCTA",
						"CACGCC",
						"ATGCAC",
						"AGAAGG"
				}), Status.OK),	//Diagonal NE Upper
				arguments(new DnaRequest(new String[] {
						"TCACTG",
						"TGATCT",
						"CGCCGA",
						"CATGCA",
						"ATGCAC",
						"AGAACG"
				}), Status.FORBIDDEN),	//Diagonal NE Lower
				arguments(new DnaRequest(new String[] {
						"TCACTG",
						"TGATCT",
						"CGCCGC",
						"CATGCA",
						"ATGCAC",
						"AGCACG"
				}), Status.OK)	//Diagonal NE Lower
				);
	}

	@MethodSource("mutantsData")
	@ParameterizedTest(name= "{index}: isMutant({0})={1}")
	public void mutantEndpointTest(DnaRequest dnaRequest, Status expectedStatus) throws IOException {

		AwsProxyRequest request = new AwsProxyRequestBuilder("/mutant", HttpMethod.POST)
				.json()
				.body(dnaRequest)
				.build();


		AwsProxyResponse response = handler.handleRequest(request, lambdaContext);

		System.out.println(dnaRequest);
		System.out.println("Status Expected: " + expectedStatus.getStatusCode() + "; Returned: " + response.getStatusCode());
		System.out.println();
		
		assertNotNull(response);
		assertEquals(expectedStatus.getStatusCode(), response.getStatusCode());
		assertFalse(response.isBase64Encoded());
		
	}

	@Test
	public void statsEndpointTest() throws JsonMappingException, JsonProcessingException {
		AwsProxyRequest request = new AwsProxyRequestBuilder("/stats", HttpMethod.GET)
				.header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
				.build();

		AwsProxyResponse response = handler.handleRequest(request, lambdaContext);
		assertNotNull(response);
		assertEquals(Status.OK.getStatusCode(), response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();
		Stats stats = mapper.readValue(response.getBody(), Stats.class);
		System.out.println(stats);
		System.out.println();

		assertFalse(stats.getCountHumanDna() == 0 && stats.getCountMutantDna() == 0);
		
	}
}
