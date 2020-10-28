package com.thelocalclass.utm.utility;

public class Constants {
	
	public static final String INVALID_REQUEST_NO_BOOLEAN = "Invalid action. Either True or False allowed";
	public static final String MESSAGE = "MESSAGE";
	public static final String INVALID_URL = "Invalid URL. URL should be like http://xyz.com";
	public static final String SUCCESS = "Success";
	public static final String DUPLICATE_ACTION = "This website is already under the same stage.";
	public static final String INPUT_CHECK_NOT_FOUND = "There is no website exists with given check number";
	public static final String INVALID_UNIT = "Unit can be only H or M (hour or minute susequently)";
	public static final String INVALID_FREQUENCY_MINUTE = "Value for minutes can be between 1 and 59";
	public static final String INVALID_FREQUENCY_HOUR = "Value for hours can be between 1 and 24";
	public static final Integer DOWNTIME_LIMIT = 2;
	public static final String INVALID_NAME_SEARCH_KEY = "Name search key should have atleast 3 characters";
	public static final String INACTIVE_URL_SEARCH_KEY = "This website is not activated. If required, you may activae it again";
	public static final String DUPLICATE_CHECK_ENTRY = "There is already a check exists in the system with this website";
	public static final String DEACTIVATED = "DEACTIVATED";
	public static final String WEBSITE_DOWN = "This website is down. System will stop monitoring it, in a while.";

}
