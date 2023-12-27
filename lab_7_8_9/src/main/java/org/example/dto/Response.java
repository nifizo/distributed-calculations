package org.example.dto;

import lombok.Data;

import java.util.List;

@Data
public class Response implements java.io.Serializable {
    public Response(ResponseStatus responseStatus, Object response) {
        status = responseStatus;
        body = response;
    }

    public enum ResponseStatus {
        SUCCESS,
        ERROR
    }

    private ResponseStatus status;
    private Object body;
}
