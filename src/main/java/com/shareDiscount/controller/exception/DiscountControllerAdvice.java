package com.shareDiscount.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.security.SignatureException;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ControllerAdvice
class DiscountControllerAdvice {

	@ExceptionHandler(value = ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody ErrorInfo
	resourceNotFoundExceptionHandler(HttpServletRequest req, Exception ex) {
		return new ErrorInfo(req.getRequestURL().toString(), ex);
	}

	@ExceptionHandler(value = ResourceAlreadyExistException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	@ResponseBody ErrorInfo
	resourceAlreadyExistExceptionHandler(HttpServletRequest req, Exception ex) {
		return new ErrorInfo(req.getRequestURL().toString(), ex);
	}

	@ResponseStatus(UNAUTHORIZED)
	@ExceptionHandler(FailedToLoginException.class)
	@ResponseBody ErrorInfo
	failedToLoginExceptionHandler(HttpServletRequest req, Exception ex) {
		return new ErrorInfo(req.getRequestURL().toString(), ex);
	}

	@ResponseStatus(FORBIDDEN)
	@ExceptionHandler(SignatureException.class)
	@ResponseBody ErrorInfo
	failedToVerifyExceptionHandler(HttpServletRequest req, Exception ex) {
		return new ErrorInfo(req.getRequestURL().toString(), ex);
	}
}
