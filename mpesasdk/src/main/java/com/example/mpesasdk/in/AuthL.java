package com.example.mpesasdk.in;

import com.example.mpesasdk.util.Response;

public interface AuthL {
    public void onAuthError(Response<Integer, String> result);

    public void onAuthSuccess();
}
