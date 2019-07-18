package com.example.mpesasdk.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;


import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Util {

    public static String sanitizePhoneNumber(String phone) {

        if (phone.equals("")) {
            return null;
        }

        if (phone.length() < 11 & phone.startsWith("0") && phone.length() >= 9) {
            String p = phone.replaceFirst("^0", "254");
            return p;
        }
        if (phone.length() == 13 && phone.startsWith("+")) {
            String p = phone.replaceFirst("^+", "");
            return p;
        }
        return null;
    }

    public static String addParamsToUrl(String url, HashMap<String, String> params) {
        if (!url.endsWith("?"))
            url += "?";
        Uri.Builder builder = Uri.parse(url).buildUpon();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }

        return builder.build().toString();
    }

    public static String getTimestamp() {
        return new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
