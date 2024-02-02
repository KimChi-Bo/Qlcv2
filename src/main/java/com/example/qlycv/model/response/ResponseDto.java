package com.example.qlycv.model.response;

import com.example.qlycv.constant.ResponseMsgEnum;
import com.example.qlycv.constant.ResponseResultEnum;

public class ResponseDto<T> {

    private ResponseResultEnum result;
    private ResponseMsgEnum msg;
    private String desc;
    private T data;

    public ResponseDto(ResponseResultEnum result, ResponseMsgEnum msg, String desc, T data) {
        this.result = result;
        this.msg = msg;
        this.desc = desc;
        this.data = data;
    }

    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<T>(ResponseResultEnum.Success, ResponseMsgEnum.OK, null, data);
    }

    public static <T> ResponseDto<T> success(T data, String desc) {
        return new ResponseDto<T>(ResponseResultEnum.Success, ResponseMsgEnum.OK, desc, data);
    }

    @SuppressWarnings("unchecked")
    public static <T, S> ResponseDto<T> fail(String error, S clazz) {
        return (ResponseDto<T>) new ResponseDto<S>(ResponseResultEnum.Error, ResponseMsgEnum.FAIL, error, null);
    }

    public ResponseResultEnum getResult() {
        return result;
    }

    public void setResult(ResponseResultEnum result) {
        this.result = result;
    }

    public ResponseMsgEnum getMsg() {
        return msg;
    }

    public void setMsg(ResponseMsgEnum msg) {
        this.msg = msg;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
