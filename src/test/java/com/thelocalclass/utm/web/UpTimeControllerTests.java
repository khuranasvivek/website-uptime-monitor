package com.thelocalclass.utm.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thelocalclass.utm.entity.WebCheck;
import com.thelocalclass.utm.service.WebCheckService;

@WebMvcTest(UpTimeController.class)
public class UpTimeControllerTests {
	
	private static final int ID = 999;
	private static final String CREATE_CHECK_ROUTE = "/utm/webcheck/create";
	private static final String CHECK_ROUTE = "/utm/webcheck/";
	private static final String CHECKS_ROUTE = "/utm/webchecks/";
	private static final String ACTIVATE_DEACTIVATE = "/activate";
    private static final String NAME = "Google";
    private static final String URL = "http://google.com";
    private static final String INTERVAL = "interval/";
    private static final int FREQUENCY = 5;
    private static final String UNIT = "M";
    private static final String NAME_URI = "name/";
	
    @Autowired
    private MockMvc mockMvc;
	
	@MockBean
	private WebCheckService webCheckServiceMock;
	
	private WebCheck check;
	
	@BeforeEach
	void init() {
		check = new WebCheck(ID, NAME, URL, FREQUENCY, UNIT, true);
	}
	
	
	/**
     *  HTTP POST /utm/webcheck/create
     */
	@Test
	@DisplayName("Create and verfiy a webcheck")
    public void createCheckTest_withCorrectDate_returnsData() throws Exception {

        when(webCheckServiceMock.createCheck(any(WebCheck.class), any(Boolean.class))).thenReturn(check);

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_CHECK_ROUTE)
                .content(new ObjectMapper().writeValueAsString(check))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.url").value(URL))
                .andExpect(MockMvcResultMatchers.jsonPath("$.frequency").value(FREQUENCY))
                .andExpect(MockMvcResultMatchers.jsonPath("$.unit").value(UNIT))
                .andExpect(MockMvcResultMatchers.jsonPath("$.active").value(true));

    }
	
	/**
     *  HTTP POST /utm/webcheck/create
     */
	@Test
	@DisplayName("Create with wrong frequency and verfiy exception")
    public void createCheckTest_withWrongDate_returnsException() throws Exception {
		
		check.setFrequency(75);

        when(webCheckServiceMock.createCheck(any(WebCheck.class), any(Boolean.class))).thenReturn(check);

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_CHECK_ROUTE)
                .content(new ObjectMapper().writeValueAsString(check))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }
	
	/**
     *  HTTP POST /utm/webcheck/{checkId}/activate/{action}
     */
	@Test
	@DisplayName("Activate existing check in the system and verify")
    public void triggerActionOnWebCheckTest_withTrue_returnsData() throws Exception {

        when(webCheckServiceMock.triggerActionOnWebCheck(anyInt(), anyBoolean())).thenReturn(check);
        
        mockMvc.perform(MockMvcRequestBuilders.post(CHECK_ROUTE+ID+ACTIVATE_DEACTIVATE+"/true")
                .content(new ObjectMapper().writeValueAsString(check))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.active").value(true));

    }
	
	/**
     *  HTTP POST /utm/webcheck/{checkId}/activate/{action}
     */
	@Test
	@DisplayName("Deactivate existing check if already under monitoring")
    public void triggerActionOnWebCheckTest_withFalse_returnsData() throws Exception {

		check.setActive(false);
        
        when(webCheckServiceMock.triggerActionOnWebCheck(anyInt(), anyBoolean())).thenReturn(check);
        
        mockMvc.perform(MockMvcRequestBuilders.post(CHECK_ROUTE+ID+ACTIVATE_DEACTIVATE+"/false")
                .content(new ObjectMapper().writeValueAsString(check))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.active").value(false));

    }
	
	/**
     *  HTTP POST /utm/webcheck/{checkId}/activate/{action}
     */
	@Test
	@DisplayName("Deactivate check with wrong boolean value and verify exception")
    public void triggerActionOnWebCheckTest_withWringBoolean_returnsException() throws Exception {

        when(webCheckServiceMock.triggerActionOnWebCheck(anyInt(), anyBoolean())).thenReturn(check);
        
        mockMvc.perform(MockMvcRequestBuilders.post(CHECK_ROUTE+ID+ACTIVATE_DEACTIVATE+"/flase")
                .content(new ObjectMapper().writeValueAsString(check))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }
	
	
	/**
     *  HTTP GET /utm/webchecks/interval/{frequency}/{unit}
     */
	@Test
	@DisplayName("Get list of webchecks that qualify given search of frequency and unit")
    public void retrieveChecksByInternvalTest_withFrequencyUnit_returnsDate() throws Exception {
		
		List<WebCheck> checks = new ArrayList<WebCheck>();
		checks.add(check);
		
        when(webCheckServiceMock.listChecksByInterval(anyInt(), anyString())).thenReturn(checks);
        
        mockMvc.perform(MockMvcRequestBuilders.get(CHECKS_ROUTE+INTERVAL+FREQUENCY+"/"+UNIT)
                .content(new ObjectMapper().writeValueAsString(check))
                .contentType(MediaType.APPLICATION_JSON))
        		.andExpect(status().isOk())
        		.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].url").value(URL))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].frequency").value(FREQUENCY))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].unit").value(UNIT))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].active").value(true));

    }
	
	/**
     *  HTTP GET /utm/webchecks/interval/{frequency}/{unit}
     */
	@Test
	@DisplayName("Get list of webchecks with wrong frequency and verify exception")
    public void retrieveChecksByInternvalTest_withWrongFrequencyValue_returnsExeption() throws Exception {
		
		List<WebCheck> checks = new ArrayList<WebCheck>();
		checks.add(check);
		
        when(webCheckServiceMock.listChecksByInterval(anyInt(), anyString())).thenReturn(checks);
        
        mockMvc.perform(MockMvcRequestBuilders.get(CHECKS_ROUTE+INTERVAL+"78"+"/"+UNIT)
                .content(new ObjectMapper().writeValueAsString(check))
                .contentType(MediaType.APPLICATION_JSON))
        		.andExpect(status().isBadRequest());

    }
	
	/**
     *  HTTP GET /utm/webchecks/interval/{frequency}/{unit}
     */
	@Test
	@DisplayName("Get list of webchecks with wrong unit and verify exception")
    public void retrieveChecksByInternvalTest_withWrongUnitValue_returnsExeption() throws Exception {
		
		List<WebCheck> checks = new ArrayList<WebCheck>();
		checks.add(check);
		
        when(webCheckServiceMock.listChecksByInterval(anyInt(), anyString())).thenReturn(checks);
        
        mockMvc.perform(MockMvcRequestBuilders.get(CHECKS_ROUTE+INTERVAL+FREQUENCY+"/"+"R")
                .content(new ObjectMapper().writeValueAsString(check))
                .contentType(MediaType.APPLICATION_JSON))
        		.andExpect(status().isBadRequest());

    }
	
	/**
     *  HTTP GET /utm/webchecks/name/{name}
     */
	@Test
	@DisplayName("Get list of webchecks with name search")
    public void retrieveChecksByNameTest_withNameSearch_returnsDate() throws Exception {
		
		List<WebCheck> checks = new ArrayList<WebCheck>();
		checks.add(check);
		
        when(webCheckServiceMock.listChecksByName(anyString())).thenReturn(checks);
        
        mockMvc.perform(MockMvcRequestBuilders.get(CHECKS_ROUTE+NAME_URI+NAME)
                .content(new ObjectMapper().writeValueAsString(check))
                .contentType(MediaType.APPLICATION_JSON))
        		.andExpect(status().isOk())
        		.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].url").value(URL))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].frequency").value(FREQUENCY))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].unit").value(UNIT))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].active").value(true));

    }
	
	/**
     *  HTTP GET /utm/webchecks/name/{name}
     */
	@Test
	@DisplayName("Get list of webchecks with worng name value and verify exception")
    public void retrieveChecksByNameTest_withWrongNameSearch_returnsException() throws Exception {
		
		List<WebCheck> checks = new ArrayList<WebCheck>();
		checks.add(check);
		
        when(webCheckServiceMock.listChecksByName(anyString())).thenReturn(checks);
        
        mockMvc.perform(MockMvcRequestBuilders.get(CHECKS_ROUTE+NAME_URI+"Go")
                .content(new ObjectMapper().writeValueAsString(check))
                .contentType(MediaType.APPLICATION_JSON))
        		.andExpect(status().isBadRequest());

    }
	
	/**
     *  HTTP GET /utm/webchecks
     */
	@Test
	@DisplayName("Get list of webchecks with worng name value and verify exception")
    public void retrieveChecksTest_returnsDate() throws Exception {
		
		List<WebCheck> checks = new ArrayList<WebCheck>();
		checks.add(check);
		
        when(webCheckServiceMock.listChecks()).thenReturn(checks);
        
        mockMvc.perform(MockMvcRequestBuilders.get(CHECKS_ROUTE)
                .content(new ObjectMapper().writeValueAsString(check))
                .contentType(MediaType.APPLICATION_JSON))
        		.andExpect(status().isOk())
        		.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].url").value(URL))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].frequency").value(FREQUENCY))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].unit").value(UNIT))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].active").value(true));

    }
	
	/**
     *  HTTP GET /utm/webchecks/name/{name}
     */
	@Test
	@DisplayName("Get list of webchecks with worng name value and verify exception")
    public void retrieveChecksTest_returnsEmpty() throws Exception {
		
        mockMvc.perform(MockMvcRequestBuilders.get(CHECKS_ROUTE)
                .content(new ObjectMapper().writeValueAsString(check))
                .contentType(MediaType.APPLICATION_JSON))
        		.andExpect(status().isOk())
        		.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").doesNotExist());

    }
	
}
