package com.thelocalclass.utm.utility;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;

import com.thelocalclass.utm.entity.WebCheck;
import com.thelocalclass.utm.exception.InvalidRequestException;

public class Utils {

    public static int HIT_TIMEOUT = 10000;

    public static String hitUrl(String url, int timeout) {
    	
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            if (200 <= responseCode && responseCode <= 399) {
            	return "UP";
            }
        } catch (IOException exception) {
            return "DOWN";
        }

        return "DOWN";
    }
    
    public static long getMilliseconds(Integer frequency, String unit) {
    	long timeInMilliseconds = 0;
    	
    	if(unit.equalsIgnoreCase("H")) {
    		timeInMilliseconds = frequency * 60 * 60 * 1000;
    	} else {
    		timeInMilliseconds = frequency * 60 * 1000;
    	}
    	return timeInMilliseconds;
    }

	public static String localDateToStringFormatter(LocalDateTime dateTime) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");
		return dateTime.format(formatter); 
		
	}
	
	public static void isValidURL(String url) {
		try { 
            new URL(url).toURI(); 
        } 
        catch (Exception e) { 
        	throw new InvalidRequestException(HttpStatus.BAD_REQUEST, Constants.INVALID_URL);
        }
    }

	public static void isValidFrequencyAndUnit(int frequency, String unit) {
		if(!(unit != null && (unit.equalsIgnoreCase("H") || unit.equalsIgnoreCase("M")))) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, Constants.INVALID_UNIT);
		}
		else {
			if(unit.equalsIgnoreCase("M") && (frequency < 1 || frequency > 59)) {
				throw new InvalidRequestException(HttpStatus.BAD_REQUEST, Constants.INVALID_FREQUENCY_MINUTE);
			}
			if(unit.equalsIgnoreCase("H") && (frequency < 1 || frequency > 24)) {
				throw new InvalidRequestException(HttpStatus.BAD_REQUEST, Constants.INVALID_FREQUENCY_HOUR);
			}
		}
	}

	public static void isValidName(String name) {
		if(name == null || name.isEmpty() || name.length() < 3) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, Constants.INVALID_NAME_SEARCH_KEY);
		}
	}

	public static void validateWebCheckInput(WebCheck webCheck) {
		isValidURL(webCheck.getUrl());
		isValidFrequencyAndUnit(webCheck.getFrequency(), webCheck.getUnit());
		isValidName(webCheck.getName());
	}

	public static boolean validateAndGetActivationAction(String action) {
		
		if(action.equalsIgnoreCase("true") || action.equalsIgnoreCase("false")) {
			return Boolean.parseBoolean(action);  
		} else {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, Constants.INVALID_REQUEST_NO_BOOLEAN);
		}
	}

	public static void sendDownTimeNotification(String url) {
		System.out.println("\n************************************************************* \n  "
				+ "Dear user \n  Your website "+url+" is down. \n  "
				+ "For now, we are deactivating it for further monitoring. \n  "
				+ "You may activate it anytime later, once back to running. \n"
				+ "*************************************************************\n");
		
	}


}
