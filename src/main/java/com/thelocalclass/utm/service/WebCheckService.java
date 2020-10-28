package com.thelocalclass.utm.service;

import java.util.List;

import com.thelocalclass.utm.entity.WebCheck;
import com.thelocalclass.utm.model.WebCheckStatusModel;

public interface WebCheckService {
	
	List<WebCheck> listChecks();
	WebCheck createCheck(WebCheck webCheck, boolean duplicateCheck);
	List<WebCheck> listChecksByName(String name);
	List<WebCheck> listChecksByInterval(int frequency, String unit);
	WebCheckStatusModel getCheckByUrl(String url);
	WebCheck triggerActionOnWebCheck(Integer checkId, boolean action);
    
}


