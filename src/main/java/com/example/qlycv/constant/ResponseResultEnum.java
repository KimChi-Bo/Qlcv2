package com.example.qlycv.constant;

public enum ResponseResultEnum {
    Success("SUCCESS"),
    Error("ERROR");

    public String val;

    private ResponseResultEnum(String val) {
        this.val = val;
    }
}
