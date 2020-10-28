package com.thelocalclass.utm.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thelocalclass.utm.entity.WebCheck;
import com.thelocalclass.utm.model.WebCheckStatusModel;
import com.thelocalclass.utm.service.WebCheckService;
import com.thelocalclass.utm.utility.Utils;

@RestController
@RequestMapping(path = "/utm")
public class UpTimeController {
	
	@Autowired
	private WebCheckService checkService;
	
	@GetMapping("/webchecks")
    public ResponseEntity<List<WebCheck>> retrieveChecks() {
		List<WebCheck> response = checkService.listChecks();
        return ResponseEntity.ok(response);
    }
	
	@GetMapping("/webchecks/name/{name}")
    public ResponseEntity<List<WebCheck>> retrieveChecksByName(@PathVariable("name") String name) {
		Utils.isValidName(name);
		List<WebCheck> response = checkService.listChecksByName(name);
        return ResponseEntity.ok(response);
    }
	
	@GetMapping("/webchecks/interval/{frequency}/{unit}")
    public ResponseEntity<List<WebCheck>> retrieveChecksByInternval(@PathVariable("frequency") int frequency, @PathVariable("unit") String unit) {
		Utils.isValidFrequencyAndUnit(frequency, unit);
		List<WebCheck> response = checkService.listChecksByInterval(frequency, unit);
        return ResponseEntity.ok(response);
    }
	
	@GetMapping("/webcheck/status/**")
    public ResponseEntity<WebCheckStatusModel> retrieveCheckByUrl(HttpServletRequest request) {
		String url = request.getRequestURL().toString();
		WebCheckStatusModel response = checkService.getCheckByUrl(url.split("/webcheck/status/")[1]);
        return ResponseEntity.ok(response);
    }
	
	@PostMapping("/webcheck/{checkId}/activate/{action}")
    public ResponseEntity<WebCheck> triggerActionOnWebCheck(@PathVariable("checkId") int checkId, @PathVariable("action") String action) {
		WebCheck response = checkService.triggerActionOnWebCheck(checkId, Utils.validateAndGetActivationAction(action));
		return ResponseEntity.ok(response);
    }
	
	@PostMapping("/webcheck/create")
	public ResponseEntity<WebCheck> createCheck(@RequestBody WebCheck webCheck) {
		Utils.validateWebCheckInput(webCheck);
		WebCheck response = checkService.createCheck(webCheck, true);
		return ResponseEntity.ok(response);
	}

}