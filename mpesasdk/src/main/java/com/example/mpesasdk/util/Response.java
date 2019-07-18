package com.example.mpesasdk.util;

public class Response<X, Y> {
    public X code;
    public Y message;

    public Response(X code, Y message) {
        this.code = code;
        this.message = message;
    }
}
