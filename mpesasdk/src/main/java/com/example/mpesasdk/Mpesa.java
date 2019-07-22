package com.example.mpesasdk;

import android.content.Context;
import android.os.AsyncTask;

import com.example.mpesasdk.in.AuthL;
import com.example.mpesasdk.in.MpesaL;
import com.example.mpesasdk.pojo.C2BTransact;
import com.example.mpesasdk.pojo.Preferences;
import com.example.mpesasdk.pojo.STKPush;
import com.example.mpesasdk.util.Api;
import com.example.mpesasdk.util.NetworkHandler;
import com.example.mpesasdk.util.Response;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Mpesa {

    private static AuthL authListener;
    private static Mpesa instance;
    private static MpesaL mpesaListener;
    private static Mode mode = Mode.SANDBOX;

    private Mpesa() {
    }

    public static void request(Context context, String key, String secret) {
        if (!(context instanceof AuthL)) {
            throw new RuntimeException("Context must implement AuthListener");
        }
        if (instance == null) {
            instance = new Mpesa();
            Preferences.getInstance().setKey(key);
            Preferences.getInstance().setSecret(secret);
            authListener = (AuthL) context;

            String url = Api.BASE_URL + Api.ACCESS_TOKEN_URL;
            if (mode == Mode.PRODUCTION)
                url = Api.PRODUCTION_BASE_URL + Api.ACCESS_TOKEN_URL;
            new AuthService().execute(url);
        }

    }
    /*
     *
     *
     *
     **/

    public static void request(Context context, String key, String secret, Mode m) {
        if (!(context instanceof AuthL)) {
            throw new RuntimeException("Context must implement AuthListener");
        }
        mode = m;
        instance = null;
        if (instance == null) {
            instance = new Mpesa();
            Preferences.getInstance().setKey(key);
            Preferences.getInstance().setSecret(secret);
            authListener = (AuthL) context;

            String url = Api.BASE_URL + Api.ACCESS_TOKEN_URL;
            if (mode == Mode.PRODUCTION)
                url = Api.PRODUCTION_BASE_URL + Api.ACCESS_TOKEN_URL;
            new AuthService().execute(url);
        }
    }
    /*
     *
     *
     *
     * */

    public static Mpesa getInstance() {
        if (instance == null) {
            throw new RuntimeException("Mpesa must be initialized with key and secret");
        }
        return instance;
    }

    /*
     *
     *
     *
     * */

    public void pay(Context context, STKPush push) {
        if (!(context instanceof AuthL)) {
            throw new RuntimeException("Context must implement MpesaListener");
        }
        if (push == null) {
            throw new RuntimeException("STKPush cannot be null");
        }
        mpesaListener = (MpesaL) context;
        JSONObject postData = new JSONObject();
        try {
            postData.put("BusinessShortCode", push.getBusinessShortCode());
            postData.put("Password", push.getPassword());
            postData.put("Timestamp", push.getTimestamp());
            postData.put("TransactionType", push.getTransactionType());
            postData.put("Amount", push.getAmount());
            postData.put("PartyA", push.getPartyA());
            postData.put("PartyB", push.getPartyB());
            postData.put("PhoneNumber", push.getPhoneNumber());
            postData.put("CallBackURL", push.getCallBackURL());
            postData.put("AccountReference", push.getAccountReference());
            postData.put("TransactionDesc", push.getTransactionDesc());

            String url = Api.BASE_URL + Api.PROCESS_REQUEST_URL;
            if (mode == Mode.PRODUCTION)
                url = Api.PRODUCTION_BASE_URL + Api.PROCESS_REQUEST_URL;
            new PayService().execute(url, postData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    public void transact(Context context, C2BTransact c2BTransact) {
//        if (!(context instanceof AuthL)) {
//            throw new RuntimeException("Context must implement MpesaListener");
//        }
//        if (c2BTransact == null) {
//            throw new RuntimeException("C2BTransact cannot be null");
//        }
//        mpesaListener = (MpesaL) context;
//        JSONObject postData = new JSONObject();
//        try {
//            postData.put("ShortCode", c2BTransact.getShortCode());
//            postData.put("CommandID", c2BTransact.getCommandID());
//            postData.put("Amount", c2BTransact.getAmount());
//            postData.put("Msisdn", c2BTransact.getMsisdn());
//            postData.put("BillRefNumber", c2BTransact.getBillRefNumber());
//
//            String url = Api.BASE_URL + Api.C2B_SIMULATE;
//
//            if (mode == Mode.PRODUCTION)
//                url = Api.PRODUCTION_BASE_URL + Api.PROCESS_REQUEST_URL;
//            new PayService().execute(url, postData.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    /*
     *
     *
     * */
    static class AuthService extends AsyncTask<String, Void, Response<Integer, String>> {

        @Override
        protected Response<Integer, String> doInBackground(String... strings) {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Basic " + Preferences.getInstance().getAuthorization());
            return NetworkHandler.doGet(strings[0], headers);
        }

        protected void onPostExecute(Response<Integer, String> result) {
            if (result == null) {
                Mpesa.getInstance();
                authListener.onAuthError(new Response<>(418, Api.ERROR));
                //User is a teapot :(
                return;
            }
            if (result.code == 400) {
                Mpesa.getInstance();
                authListener.onAuthError(new Response<>(result.code, "Invalid credentials"));
                return;
            }
            try {
                JsonParser jsonParser = new JsonParser();
                JsonObject jo = (JsonObject) jsonParser.parse(result.message);
                if (result.code / 100 != 2) {
                    //Error occurred
                    String message =  jo.get("errorMessage").getAsString();
                    Mpesa.getInstance();
                    authListener.onAuthError(new Response<>(result.code, message));
                    return;
                }
                String access_token =  jo.get("access_token").getAsString();
                Preferences.getInstance().setAccessToken(access_token);
                Mpesa.getInstance();
                authListener.onAuthSuccess();
            } catch (Exception e) {
                String message = "Error completing fetching token.Please try again.";
                Mpesa.getInstance();
                authListener.onAuthError(new Response<>(result.code, message));
            }
        }
    }

    /*
     *
     *
     * */

    static class PayService extends AsyncTask<String, Void, Response<Integer, String>> {
        @Override
        protected Response<Integer, String> doInBackground(String... strings) {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + Preferences.getInstance().getAccessToken());
            return NetworkHandler.doPost(strings[0], strings[1], headers);
        }

        protected void onPostExecute(Response<Integer, String> result) {
            if (result == null) {
                Mpesa.getInstance();
                mpesaListener.onMpesaError(new Response<>(418, Api.ERROR)); //User is a teapot :(
                return;
            }
            try {
                JsonParser jsonParser = new JsonParser();

                JsonObject jo = (JsonObject) jsonParser.parse(result.message);
                if (result.code / 100 != 2) {
                    //Error occurred
                    if (jo.has("errorMessage")) {
                        String message =  jo.get("errorMessage").getAsString();
                        Mpesa.getInstance();
                        mpesaListener.onMpesaError(new Response<>(result.code, message));
                        return;
                    }
                    String message = "Error completing payment.Please try again.";
                    Mpesa.getInstance();
                    mpesaListener.onMpesaError(new Response<>(result.code, message));
                    return;
                }
                Mpesa.getInstance();
                mpesaListener.onMpesaSuccess(jo.get("MerchantRequestID").toString(), jo.get("CheckoutRequestID").toString(), jo.get("CustomerMessage").toString());
            } catch (Exception e) {
                String message = "Error completing payment.Please try again.";
                Mpesa.getInstance();
                mpesaListener.onMpesaError(new Response<>(result.code, message));
            }
        }
    }


}
