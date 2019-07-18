package com.example.mpesasdk.pojo;


import com.example.mpesasdk.util.Api;
import com.example.mpesasdk.util.Util;

import android.util.Base64;

public class STKPush {
    private String businessShortCode;
    private String password;
    private String timestamp;
    private String transactionType;
    private String amount;
    private String partyA;
    private String partyB;
    private String phoneNumber;
    private String callBackURL;
    private String accountReference;
    private String transactionDesc;


    private STKPush(Simulate simulate) {
        this.businessShortCode = simulate.businessShortCode;
        this.timestamp = Util.getTimestamp();
        this.password = getPassword(simulate.businessShortCode, simulate.passkey, this.timestamp);
        this.transactionType = simulate.transactionType;
        this.amount = String.valueOf(simulate.amount);
        this.partyA = simulate.partyA;
        this.partyB = simulate.partyB;
        this.phoneNumber = simulate.phoneNumber;
        this.accountReference = simulate.accountReference;
        this.transactionDesc = simulate.transactionDesc;
        this.callBackURL = simulate.callBackURL;
    }

    private String getPassword(String businessShortCode, String passkey, String timestamp) {
        String str = businessShortCode + passkey + timestamp;
        return Base64.encodeToString(str.getBytes(), Base64.NO_WRAP);
    }

    public static class Simulate {
        private String businessShortCode;
        private String passkey;
        private String transactionType = Api.DEFAULT_TRANSACTION_TYPE;
        private int amount;
        private String partyA;
        private String partyB;
        private String phoneNumber;
        private String callBackURL;
        private String accountReference;
        private String transactionDesc;

        public Simulate(String businessShortCode, String passkey, int amount, String partyA, String partyB, String phoneNumber, String callBackURL) {


            this.businessShortCode = businessShortCode;
            this.passkey = passkey;
            this.amount = amount;
            this.partyA = Util.sanitizePhoneNumber(phoneNumber);
            this.partyB = partyB;
            this.phoneNumber = Util.sanitizePhoneNumber(phoneNumber);
            this.callBackURL = callBackURL;
            this.accountReference = Util.sanitizePhoneNumber(phoneNumber);
            this.transactionDesc = Util.sanitizePhoneNumber(phoneNumber);
        }


        public Simulate setTransactionType(String transactionType) {
            this.transactionType = transactionType;
            return this;
        }

        public Simulate setDescription(String description) {
            this.transactionDesc = description;
            return this;
        }

        public Simulate setAccountReference(String accountReference) {
            this.accountReference = accountReference;
            return this;
        }

        public STKPush build() {
            return new STKPush(this);
        }
    }

    public String getBusinessShortCode() {
        return businessShortCode;
    }

    public String getPassword() {
        return password;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getAmount() {
        return amount;
    }

    public String getPartyA() {
        return partyA;
    }

    public String getPartyB() {
        return partyB;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCallBackURL() {
        return callBackURL;
    }

    public String getAccountReference() {
        return accountReference;
    }

    public String getTransactionDesc() {
        return transactionDesc;
    }


}
