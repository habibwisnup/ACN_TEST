package com.BNI.habib.Test.Constant;

public enum ResponseCode {
    DATA_NOT_FOUND( "30000"
            , "ERROR: Cannot find resource with id : "),
    INVALID_END_DATE_FORMAT("INVALID_END_DATE_FORMAT"
            , "ERROR: Invalid End Date format at row : "),
    BILLING_CYCLE_NOT_ON_RANGE ("BILLING_CYCLE_NOT_ON_RANGE"
            ,"ERROR: Billing Cycle not on range at row :"),
    DATA_DELETE("HTTP 204"
            ,"Succesfully Deleted"),
    DUPLICATE_DATA ("30001"
            ,"Record With Unique Value"),
    INVALID_DATA ("30002"
            ,"invalid value"),
    SUCCESS("SUCCESS", "SUCCESS"),
    SYSTEM_ERROR("SYSTEM_ERROR", "Contact our team"),
    RUNTIME_ERROR("RUNTIME_ERROR", "Runtime Error"),
    BIND_ERROR("BIND_ERROR"
            , "Please fill in mandatory parameter");
    private String code;
    private String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
