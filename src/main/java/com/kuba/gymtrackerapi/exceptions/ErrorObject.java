package com.kuba.gymtrackerapi.exceptions;

import java.util.Date;

public record ErrorObject(Integer statusCode, String message, Date timeStamp) {
}
