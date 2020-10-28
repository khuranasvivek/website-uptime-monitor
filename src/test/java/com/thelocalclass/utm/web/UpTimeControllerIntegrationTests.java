package com.thelocalclass.utm.web;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestClientException;

import com.thelocalclass.utm.entity.WebCheck;
import com.thelocalclass.utm.model.WebCheckStatusModel;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/testdata.sql")
public class UpTimeControllerIntegrationTests {
	
	private static final String URL_DOES_NOT_EXIST_SEARCH_KEY = "http://doesnotexist.com";
	private static final String SEARCH_CHECK_BY_NAME_ROUTE = "/utm/webchecks/name/";
    private static final String SEARCH_CHECK_BY_URL_ROUTE = "/utm/webcheck/status/";
    private static final String WEB_CHECK_CREATE_URL_ROUTE = "/utm/webcheck/create";
    private static final String WEB_CHECK_DEACTIVATE_URL_ROUTE = "/utm/webcheck/2/activate/false";
    private static final String WEB_CHECKS_WITH_INTERVAL_URL_ROUTE = "/utm/webchecks/interval/1/M";
    private static final String URL_UP_SEARCH_KEY = "http://google.com";
	private static final String URL_DOWN_SEARCH_KEY = "http://fakesite.com";
	private static final String NAME_SEARCH_KEY = "API";
	private static final String NAME_SEARCH_KEY_DOES_NOT_EXIST = "UTMUTMUTM";
	private static final String WEB_CHECKS_URL_ROUTE = "/utm/webchecks";
	

    
    private static final String STATUS_UP = "UP";
    private static final String SINCE = "11-May-2020 19:16";
    private static final String AVERAGE_RESPONSE = "312 ms";
	
	@Autowired
    private TestRestTemplate restTemplate;
	
	/**
	 * HTTP GET /webchecks/name/{name}
	 */
	@Test
	@DisplayName("Get all checks active in database that have API in their name")
	public void retrieveChecksByNameTest_withValidSearch_returnsData() {

		 ResponseEntity<List<WebCheck>> response = restTemplate.exchange(SEARCH_CHECK_BY_NAME_ROUTE+NAME_SEARCH_KEY, HttpMethod.GET, null,
                 new ParameterizedTypeReference<List<WebCheck>>() {});
        
		 assertEquals(response.getStatusCode(), HttpStatus.OK);
		 assertEquals(response.getBody().size(), 2);
		
	}
	
	
	/**
	 * HTTP GET /webchecks/name/{name}
	 */
	@Test
	@DisplayName("Get all checks active in database that have UTMUTMUTM in their name")
	public void retrieveChecksByNameTest_withValidSearch_returnsNull() {

		 ResponseEntity<List<WebCheck>> response = restTemplate.exchange(SEARCH_CHECK_BY_NAME_ROUTE+NAME_SEARCH_KEY_DOES_NOT_EXIST, HttpMethod.GET, null,
                 new ParameterizedTypeReference<List<WebCheck>>() {});
        
		 assertEquals(response.getStatusCode(), HttpStatus.OK);
		 assertEquals(response.getBody().size(), 0);
		
	}
	
	/**
     * HTTP GET /utm/webcheck/status/{url}
	 * @throws URISyntaxException 
	 * @throws RestClientException 
     */
	@Test
	@DisplayName("Get check in database with URL search key and status up")
    public void retrieveCheckByUrlTest_withValidInput_statusUp_returnsData() throws RestClientException, URISyntaxException {

        ResponseEntity<WebCheckStatusModel> response = restTemplate.getForEntity(new URI(SEARCH_CHECK_BY_URL_ROUTE+URL_UP_SEARCH_KEY), WebCheckStatusModel.class);
        
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().getStatus(), STATUS_UP);
        assertEquals(response.getBody().getSince(), SINCE);
        assertEquals(response.getBody().getAverageResponse(), AVERAGE_RESPONSE);

    }
	
	/**
     * HTTP GET /utp/webcheck/status/{url}
	 * @throws URISyntaxException 
	 * @throws RestClientException 
     */
	@Test
	@DisplayName("Get check in database with URL search key and status down")
    public void retrieveCheckByUrlTest_withValidInput_statusDown_returnsException() throws RestClientException, URISyntaxException {

        ResponseEntity<WebCheckStatusModel> response = restTemplate.getForEntity(new URI(SEARCH_CHECK_BY_URL_ROUTE+URL_DOWN_SEARCH_KEY), WebCheckStatusModel.class);
        
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
	
	/**
     *  HTTP GET /utm/webcheck/status/{url}
     */
	@Test
	@DisplayName("Get check in database with URL search key but does not exists")
    public void retrieveCheckByUrlTest_withValidInput_urlDoesNotExist_returnsNull() {

        ResponseEntity<WebCheckStatusModel> response = restTemplate.getForEntity(SEARCH_CHECK_BY_URL_ROUTE+URL_DOES_NOT_EXIST_SEARCH_KEY, WebCheckStatusModel.class);
        
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), null);

    }
	
	/**
     * HTTP POST /utm/webcheck/create
	 * @throws JSONException 
     */
	@Test
	@DisplayName("Creates a check in database and then validates it")
	@Sql("/cleandata.sql")
    public void createCheckTest_withValidInput_returnsData() throws JSONException, RestClientException, URISyntaxException, InterruptedException {
		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        JSONObject check = new JSONObject();
		check.put("name", "Google");
		check.put("url", "http://google.com");
		check.put("frequency", 15);
		check.put("unit", "M");
		
		HttpEntity<String> request = new HttpEntity<String>(check.toString(), headers);
		
        ResponseEntity<WebCheck> response = restTemplate.postForEntity(WEB_CHECK_CREATE_URL_ROUTE, request, WebCheck.class);
        
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().getName(), "Google");
        assertEquals(response.getBody().getFrequency(), 15);
        
        TimeUnit.SECONDS.sleep(3);
        
        ResponseEntity<WebCheckStatusModel> monitorResponse = restTemplate.getForEntity(new URI(SEARCH_CHECK_BY_URL_ROUTE+"http://google.com"), WebCheckStatusModel.class);
        
        assertEquals(monitorResponse.getStatusCode(), HttpStatus.OK);
        assertEquals(monitorResponse.getBody().getStatus(), STATUS_UP);
    }
	
	
	/**
     *  HTTP POST /utm/webcheck/{checkId}/activate/{action}
	 * @throws JSONException
     */
	@Test
	@DisplayName("Deactivate monitoring for a check.")
    public void triggerActionOnWebCheckTest_withValidInput_returnsData() throws JSONException {
		
        ResponseEntity<WebCheck> response = restTemplate.postForEntity(WEB_CHECK_DEACTIVATE_URL_ROUTE, new WebCheck(), WebCheck.class);
        
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().getActive(), false);
    }
	
	/**
     *  HTTP GET /utm/webchecks
     */
	@Test
	@DisplayName("Get all active checks in database")
    public void retrieveChecksTest_returnsData() {

		ResponseEntity<List<WebCheck>> response = restTemplate.exchange(WEB_CHECKS_URL_ROUTE, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<WebCheck>>() {});
        
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().size(), 3);

    }
	
	/**
     *  HTTP GET /utm/webchecks/interval/{frequency}/{unit}
     */
	@Test
	@DisplayName("Get all checks with given interval")
    public void retrieveChecksByInternvalTest_returnsData() {

		ResponseEntity<List<WebCheck>> response = restTemplate.exchange(WEB_CHECKS_WITH_INTERVAL_URL_ROUTE, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<WebCheck>>() {});
        
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().size(), 2);

    }

}
