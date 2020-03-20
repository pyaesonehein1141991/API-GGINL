package org.tat.gginl.api.exception;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
@RestControllerAdvice
public class GlobalExceptionHandlerController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Bean
	  public ErrorAttributes errorAttributes() {
	    // Hide exception field in the return object
	    return new DefaultErrorAttributes() {
	      @Override
	      public Map<String, Object> getErrorAttributes(WebRequest requestAttributes, boolean includeStackTrace) {
	        Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
	        errorAttributes.remove("exception");
	        logger.error(errorAttributes.toString());
	        return errorAttributes;
	      }
	    };
	  }
  @ExceptionHandler(CustomException.class)
  public void handleCustomException(HttpServletResponse res, CustomException ex) throws IOException {
	  logger.error("handleCustomException: " + ex.getMessage());
    res.sendError(ex.getHttpStatus().value(), ex.getMessage());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public void handleAccessDeniedException(HttpServletResponse res) throws IOException {
	  logger.error("Access Denied");
    res.sendError(HttpStatus.FORBIDDEN.value(), "Access denied");
  }

  @ExceptionHandler(Exception.class)
  public void handleException(HttpServletResponse res) throws IOException {
	logger.error("IOEXception :");
    res.sendError(HttpStatus.BAD_REQUEST.value(), "Something went wrong");
  }
  
  
  @ExceptionHandler(SystemException.class)
  public void handleDAOException(HttpServletResponse res,SystemException e) throws IOException {
	logger.error("SystemException :");
	if(ErrorCode.SYSTEM_ERROR_RESOURCE_NOT_FOUND.equals(e.getErrorCode())) {
		  res.sendError(HttpStatus.NOT_FOUND.value(),e.getMessage());
	  }else if(ErrorCode.NRC_FORMAT_NOT_MATCH.equals(e.getErrorCode())){
		  res.sendError(HttpStatus.BAD_REQUEST.value(),e.getMessage());
	  }else if(ErrorCode.PAYMENT_ALREADY_CONFIRMED.equals(e.getErrorCode())) {
		  res.sendError(HttpStatus.BAD_REQUEST.value(),e.getMessage());
	  }
	else {
		 res.sendError(HttpStatus.BAD_REQUEST.value(), "Something went wrong");
	  }
  }
}
