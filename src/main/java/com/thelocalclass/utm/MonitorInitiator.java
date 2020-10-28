package com.thelocalclass.utm;

import com.thelocalclass.utm.entity.WebCheck;
import com.thelocalclass.utm.service.MonitorService;

public class MonitorInitiator {
	
	private final WebCheck webCheck;
    private final MonitorService monitorService;


	public MonitorInitiator(WebCheck webCheck, MonitorService monitorService) {
		this.webCheck = webCheck;
		this.monitorService = monitorService;
	}
	
	
	public WebCheck getWebCheck() {
		return webCheck;
	}

	public void monitor() {
		monitorService.startMonitor(webCheck);
    }

}
