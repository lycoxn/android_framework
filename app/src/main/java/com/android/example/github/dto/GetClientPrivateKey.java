package com.android.example.github.dto;

public class GetClientPrivateKey {
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "GetClientPrivateKey{" +
                "key='" + key + '\'' +
                '}';
    }
}
