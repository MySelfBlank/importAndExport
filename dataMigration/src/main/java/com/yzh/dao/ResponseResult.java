package com.yzh.dao;

import java.util.Date;

public class ResponseResult {

    private  Integer status;

    private  String message;

    private  Object data;

    private  String[] exceptions;

    private  Date timestamp;

    public ResponseResult(Integer status) {
        this.status = status;
    }

    public ResponseResult() {
        super();
        this.status = null;
        this.message = null;
        this.data = null;
        this.timestamp = new Date();
        this.exceptions = null;
    }

    public ResponseResult(Integer status, String message, Object data, String[] exceptions, Date timestamp) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.exceptions = exceptions;
        this.timestamp = timestamp;
    }


    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public String[] getExceptions() {
        return exceptions;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setExceptions(String[] exceptions) {
        this.exceptions = exceptions;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
