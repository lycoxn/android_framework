package com.android.example.github.dto;

public class GetClientPrivateKeyBean extends BaseNetCode {
    private GetClientPrivateKey data;

    public GetClientPrivateKey getData() {
        return data;
    }

    public void setData(GetClientPrivateKey data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "GetClientPrivateKeyBean{" +
                "data=" + data +
                '}';
    }
}