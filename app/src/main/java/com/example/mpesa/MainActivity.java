package com.example.mpesa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mpesasdk.Mpesa;
import com.example.mpesasdk.in.AuthL;
import com.example.mpesasdk.in.MpesaL;
import com.example.mpesasdk.pojo.C2BTransact;
import com.example.mpesasdk.pojo.STKPush;
import com.example.mpesasdk.util.Api;
import com.example.mpesasdk.util.Response;

public class MainActivity extends AppCompatActivity implements AuthL, MpesaL {
    private static final String TAG = "MainActivity";

    public static final String BUSINESS_SHORT_CODE = "174379";
    public static final String PASSKEY = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";
    public static final String CONSUMER_KEY = "hAVnRxa2UOjyAnydVJMG31A0OuDDCxm5";
    public static final String CONSUMER_SECRET = "UcpmdCdI8bAakdgm";
    public static final String CALLBACK_URL = "https://www.erickogi.co.ke/Mpesaphp/callback.php";

    public static final String Shortcode1 = "600535";
    public static final String InitiatorNameShortcode1 = "testapi";
    public static final String SecurityCredentialShortcode1 = "Safaricom535!";
    public static final String TestMSISDN = "254708374149";
    private static final String Shortcode2 = "600000";



    Button mPay;
    EditText mPhone, mAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Mpesa.request(this, CONSUMER_KEY, CONSUMER_SECRET);

        mPhone = findViewById(R.id.edit_phone);
        mAmount = findViewById(R.id.edit_amount);


        mPay = findViewById(R.id.mpesa);
        mPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mPhone.getText().toString();
                int  amount =  Integer.valueOf(mAmount.getText().toString());
                if (phone.isEmpty()) {
                    mPhone.setError("Enter phone.");
                    return;
                }
                pay(phone, amount);
            }
        });
    }

    private void pay(String phone, int amount) {
        STKPush.Simulate simulate = new STKPush.Simulate(BUSINESS_SHORT_CODE,
                PASSKEY,
                amount,
                phone,
                BUSINESS_SHORT_CODE,
                phone,
                CALLBACK_URL);

        STKPush push = simulate.build();
        Mpesa.getInstance().pay(this, push);

//            C2BTransact.Simulate simulate = new C2BTransact.Simulate(
//                    Shortcode1,
//                    Api.BUYGOODS,
//                    amount,
//                    phone,
//                    "xxxxxxxxxxxxxxxxxxxx"
//            );
//            C2BTransact push = simulate.build();

//            Mpesa.getInstance().transact(this, push);


    }


    @Override
    public void onAuthError(Response<Integer, String> result) {
        Log.e(TAG, result.message);
    }

    @Override
    public void onAuthSuccess() {
        mPay.setEnabled(true);


    }

    @Override
    public void onMpesaError(Response<Integer, String> result) {
        Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onMpesaSuccess(String MerchantRequestID, String CheckoutRequestID, String CustomerMessage) {
        Toast.makeText(this, CustomerMessage, Toast.LENGTH_SHORT).show();
    }
}
