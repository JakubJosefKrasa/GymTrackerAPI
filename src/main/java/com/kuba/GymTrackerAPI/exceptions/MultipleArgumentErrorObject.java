package com.kuba.GymTrackerAPI.exceptions;

import java.util.Date;
import java.util.Map;

public record MultipleArgumentErrorObject(Integer statusCode, Map<String, String> errors, Date timeStamp) {
}
