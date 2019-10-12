package com.android.example.github.dto;

public class BaseNetCode {

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        if (code.equals("03000002")){
            return "登录信息已失效或已在其他设备登录";
        }
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BaseNetCode{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
