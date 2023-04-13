package com.BNI.habib.Test.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private String code;
    private String message;
    private List<String> errors;
    private T data;
    private Date serverTime;

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", errors=" + errors +
                ", data=" + data +
                ", serverTime=" + serverTime +
                '}';
    }
}
