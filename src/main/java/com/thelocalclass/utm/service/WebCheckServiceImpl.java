package com.thelocalclass.utm.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.stereotype.Service;

import com.thelocalclass.utm.MonitorInitiator;
import com.thelocalclass.utm.entity.Monitor;
import com.thelocalclass.utm.entity.WebCheck;
import com.thelocalclass.utm.exception.InvalidRequestException;
import com.thelocalclass.utm.model.WebCheckStatusModel;
import com.thelocalclass.utm.repository.CheckRepository;
import com.thelocalclass.utm.repository.MonitorReposoitory;
import com.thelocalclass.utm.utility.Constants;
import com.thelocalclass.utm.utility.Utils;
import com.thelocalclass.utm.utility.UpTimeMonitorScheduler;

@Service
public class WebCheckServiceImpl implements WebCheckService {
    
	@Autowired
    private CheckRepository checkRepository;
	
	@Autowired
	private MonitorService monitorService;
	
	@Autowired
	private MonitorReposoitory monitorReposoitory;
	
	@Autowired
    private TaskScheduler taskScheduler;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	UpTimeMonitorScheduler upTimeMonitorScheduler;
    
    @Override
    public List<WebCheck> listChecks() {
    	return checkRepository.findAll();
    }
    
    @Override
    public WebCheck createCheck(WebCheck webCheck, boolean duplicateCheck) {
    	if(duplicateCheck && checkRepository.findByUrlIgnoreCase(webCheck.getUrl()).isPresent()) {
    		throw new InvalidRequestException(HttpStatus.BAD_REQUEST, Constants.DUPLICATE_CHECK_ENTRY);
    	}
    	webCheck.setUrl(webCheck.getUrl().toLowerCase());
    	webCheck = checkRepository.save(webCheck);
    	
    	if(webCheck.getActive()) {
    		long frequency = Utils.getMilliseconds(webCheck.getFrequency(), webCheck.getUnit());
    		
    		MonitorInitiator monitor = new MonitorInitiator(webCheck, monitorService);
        	try {
                taskScheduler.scheduleAtFixedRate(new ScheduledMethodRunnable(monitor, "monitor"), frequency);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
    	} else {
    		upTimeMonitorScheduler.cancelScheduledPolling(webCheck.getId());
    		monitorReposoitory.save(new Monitor(Constants.DEACTIVATED, 0L, webCheck.getId(), LocalDateTime.now(), LocalDateTime.now(), 0));
    	}
    	
    	return webCheck;
    }

	@Override
	public List<WebCheck> listChecksByName(String name) {
		List<WebCheck> checks = checkRepository.findByName(name);
		if(checks.isEmpty()) {
			checks = checkRepository.findByNameIgnoreCaseContaining(name);
		}
		return checks;
	}

	@Override
	public List<WebCheck> listChecksByInterval(int frequency, String unit) {
		return checkRepository.findByFrequencyAndUnitIgnoreCase(frequency, unit);
	}

	@Override
	public WebCheckStatusModel getCheckByUrl(String url) {
		WebCheckStatusModel model  = null;
		Optional<WebCheck> check = checkRepository.findByUrlIgnoreCase(url);
		
		if(check.isPresent()) {
			if(!check.get().getActive()) {
				throw new InvalidRequestException(HttpStatus.BAD_REQUEST, Constants.INACTIVE_URL_SEARCH_KEY);
			}
			List<Monitor> monitors = monitorService.getLastHundredRecordsByCheckIdAndStatus(check.get().getId(), "UP");
			Optional<Monitor> monitor = monitorService.getLastRecordByCheckId(check.get().getId());
			
			if(!monitors.isEmpty()) {
				long average = monitors.stream().mapToLong(m -> m.getResponseTime()).sum()/monitors.size();
				model =  modelMapper.map(monitor.get(), WebCheckStatusModel.class);
				model.setSince(Utils.localDateToStringFormatter(monitor.get().getSince()));
				model.setAverageResponse(average+" ms");
			} else {
				throw new InvalidRequestException(HttpStatus.BAD_REQUEST, Constants.WEBSITE_DOWN);
			}
		}
		return model;
	}

	@Override
	public WebCheck triggerActionOnWebCheck(Integer checkId, boolean action) {
		Optional<WebCheck> check = checkRepository.findById(checkId);
		
		if(check.isPresent()) {
			if(check.get().getActive() != action) {
				WebCheck webCheck = check.get();
				webCheck.setActive(action);
				return createCheck(webCheck, false);
			} else {
				throw new InvalidRequestException(HttpStatus.BAD_REQUEST, Constants.DUPLICATE_ACTION);
			}
		}
		throw new InvalidRequestException(HttpStatus.BAD_REQUEST, Constants.INPUT_CHECK_NOT_FOUND);
	}

}
