package com.BNI.habib.Test.Controllers;

import com.BNI.habib.Test.Constant.ResponseCode;
import com.BNI.habib.Test.Exception.BusinessLogicException;
import com.BNI.habib.Test.Exception.ValidationException;
import com.BNI.habib.Test.Response.BaseResponse;
import com.BNI.habib.Test.Response.CommonResponse;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.ArrayList;
import java.util.List;

@Hidden
@RestControllerAdvice
public class ErrorHandlerController {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ErrorHandlerController.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse exception(Exception e) {
        LOGGER.warn("Exception = {}", e);
        return CommonResponse.constructResponse(
                ResponseCode.SYSTEM_ERROR.getCode(),
                ResponseCode.SYSTEM_ERROR.getMessage(), null, null);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse handleValidationException(ValidationException ex) {
        return CommonResponse.constructResponse(
                ResponseCode.INVALID_DATA.getCode(),
                ResponseCode.INVALID_DATA.getMessage(), ex.getErrorMessages(), null);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public BaseResponse bindException(WebExchangeBindException be) {
        LOGGER.info("BindException = {}", be);
        List<FieldError> bindErrors = be.getFieldErrors();
        List<String> errors = new ArrayList<>();
        for (FieldError fieldError : bindErrors) {
            errors.add(
                    fieldError.getField() + " " + fieldError.getDefaultMessage());
        }

        return CommonResponse.constructResponse(
                ResponseCode.BIND_ERROR.getCode(),
                ResponseCode.BIND_ERROR.getMessage(), errors, null);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse runTimeException(RuntimeException re) {
        LOGGER.info("Runtime Error = {}", re);
        return CommonResponse.constructResponse(
                ResponseCode.RUNTIME_ERROR.getCode(),
                ResponseCode.RUNTIME_ERROR.getMessage(), null, null);
    }

    @ExceptionHandler(BusinessLogicException.class)
    public BaseResponse businessLogicException(BusinessLogicException ble) {
        LOGGER.info("BusinessLogicException = {}", ble);
        return CommonResponse.constructResponse(ble.getCode(), ble.getMessage(),
                null, null);
    }
}
