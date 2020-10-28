package com.thelocalclass.utm.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Stopwatch;
import com.thelocalclass.utm.entity.Monitor;
import com.thelocalclass.utm.entity.WebCheck;
import com.thelocalclass.utm.repository.MonitorReposoitory;
import com.thelocalclass.utm.utility.Constants;
import com.thelocalclass.utm.utility.Utils;
import com.thelocalclass.utm.utility.UpTimeMonitorScheduler;

@Service
public class MonitorServiceImpl implements MonitorService {
	
	@Autowired
	private MonitorReposoitory monitorReposoitory;
	
	@Autowired
	private WebCheckService webCheckService;
	
	@Autowired
	private UpTimeMonitorScheduler upTimeMonitorScheduler;
	
	@Override
	public List<Monitor> getLastHundredRecordsByCheckIdAndStatus(Integer checkId, String status) {
		return monitorReposoitory.findTop100ByCheckIdAndStatusOrderByIdDesc(checkId, status);
	}
	
	@Override
	public Optional<Monitor> getLastRecordByCheckId(Integer checkId) {
		return monitorReposoitory.findTop1ByCheckIdOrderByIdDesc(checkId);
	}

	@Override
	public void startMonitor(WebCheck webCheck) {
        
		Stopwatch timeTracker = Stopwatch.createStarted();
        String status = Utils.hitUrl(webCheck.getUrl(), Utils.HIT_TIMEOUT);
        timeTracker.stop();
        
        ScheduledFuture<?> scheduledMonitor = upTimeMonitorScheduler.getScheduledMonitor(webCheck.getId());
        
        if (scheduledMonitor == null) {
        	return;
        }
        synchronized (scheduledMonitor) {
            if (upTimeMonitorScheduler.getScheduledMonitor(webCheck.getId()) == null) {
            	return;
            }
            
            Monitor monitor = getMonitor(status, webCheck, timeTracker);
            if(monitor != null) {
            	monitorReposoitory.save(monitor);
            }
            
        }
    }

	private Monitor getMonitor(String status, WebCheck webCheck, Stopwatch timeTracker) {
		
		Monitor monitor = null;
		LocalDateTime since, last;
		since = last = LocalDateTime.now();
		Long responseTime = timeTracker.elapsed(TimeUnit.MILLISECONDS);
		int tracker = 0;
		
		Optional<Monitor> existingMonitor = monitorReposoitory.findTop1ByCheckIdOrderByIdDesc(webCheck.getId());

		if(existingMonitor.isPresent()) {
			monitor = existingMonitor.get();
			since = (!monitor.getStatus().equals(status)) ? LocalDateTime.now() : monitor.getSince();
			
			if(status.equals("DOWN")) {
				
				if(monitor.getTracker() >= Constants.DOWNTIME_LIMIT) {
					upTimeMonitorScheduler.cancelScheduledPolling(webCheck.getId());
					webCheck.setActive(false);
					webCheckService.createCheck(webCheck, false);
					Utils.sendDownTimeNotification(webCheck.getUrl());
					return monitor;
				}
				
				tracker = monitor.getTracker()+1;
			} else {
				tracker = 0;
			}
		}
		
		monitor = new Monitor(status, responseTime, webCheck.getId(), since, last, tracker);
		
		return monitor;
	}

}
