package com.example.mpesasdk.in;

import com.example.mpesasdk.util.Response;

public interface MpesaL {
    void onMpesaError(Response<Integer, String> result);

    void onMpesaSuccess(String MerchantRequestID, String CheckoutRequestID, String CustomerMessage);

}
