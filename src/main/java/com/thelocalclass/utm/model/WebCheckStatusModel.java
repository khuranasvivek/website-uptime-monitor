package com.thelocalclass.utm.model;

public class WebCheckStatusModel {
	
    private String status;
    
    private String since;
    
    private String averageResponse;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSince() {
		return since;
	}

	public void setSince(String since) {
		this.since = since;
	}

	public String getAverageResponse() {
		return averageResponse;
	}

	public void setAverageResponse(String averageResponse) {
		this.averageResponse = averageResponse;
	}

}
