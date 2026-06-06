package com.wo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private int code;
    private String message;
    private T data;
    private PageInfo meta;
    private long timestamp;

    public static <T> Result<T> ok(T data) {
        Result<T> r = new Result<>();
        r.code = 200;
        r.message = "success";
        r.data = data;
        r.timestamp = System.currentTimeMillis();
        return r;
    }

    public static <T> Result<T> ok(T data, PageInfo meta) {
        Result<T> r = ok(data);
        r.meta = meta;
        return r;
    }

    public static <T> Result<T> fail(String message) {
        Result<T> r = new Result<>();
        r.code = 400;
        r.message = message;
        r.timestamp = System.currentTimeMillis();
        return r;
    }

    public static <T> Result<T> fail(int code, String message) {
        Result<T> r = new Result<>();
        r.code = code;
        r.message = message;
        r.timestamp = System.currentTimeMillis();
        return r;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PageInfo {
        private int page;
        private int size;
        private long total;
    }
}
