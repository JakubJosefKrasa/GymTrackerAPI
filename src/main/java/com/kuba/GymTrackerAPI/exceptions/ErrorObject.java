package com.kuba.GymTrackerAPI.exceptions;

import java.util.Date;

public record ErrorObject(Integer statusCode, String message, Date timeStamp) {
}
