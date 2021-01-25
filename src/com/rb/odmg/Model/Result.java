package com.rb.odmg.Model;

public class Result {

    private boolean success;
    private String errMsg;
    private int value;

    public Result(boolean success, String errMsg, int value) {
        this.success = success;
        this.errMsg = errMsg;
        this.value = value;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
