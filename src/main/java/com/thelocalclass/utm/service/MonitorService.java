package com.thelocalclass.utm.service;

import java.util.List;
import java.util.Optional;

import com.thelocalclass.utm.entity.Monitor;
import com.thelocalclass.utm.entity.WebCheck;

public interface MonitorService {

	void startMonitor(WebCheck webCheck);

	List<Monitor> getLastHundredRecordsByCheckIdAndStatus(Integer checkId, String status);
	Optional<Monitor> getLastRecordByCheckId(Integer checkId);

}
