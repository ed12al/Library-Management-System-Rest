package com.gcit.lms.utils;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);
	
	@SuppressWarnings("rawtypes")
	@ExceptionHandler({DataIntegrityViolationException.class, NullPointerException.class, ParseException.class})
	public ResponseEntity handleBadRequest(Exception e){
		logger.warn(e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
    @SuppressWarnings("rawtypes")
	@ExceptionHandler(DataAccessException.class)
    public ResponseEntity handleSQLException(Exception e) {
    	logger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
	
	@SuppressWarnings("rawtypes")
	@ExceptionHandler(Exception.class)
	public ResponseEntity handleException(Exception e){
		logger.warn(e.getMessage());
		return ResponseEntity.status(500).build();
	}
}
