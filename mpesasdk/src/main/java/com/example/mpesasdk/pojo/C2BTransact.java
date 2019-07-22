package com.example.mpesasdk.pojo;

import com.example.mpesasdk.util.Util;

public class C2BTransact {

    private String ShortCode;
    private String CommandID;
    private String Amount;
    private String Msisdn;
    private String BillRefNumber;
    private int Production;

    private C2BTransact(Simulate simulate) {
        this.ShortCode = simulate.ShortCode;
        this.CommandID = simulate.CommandID;
        this.Amount = simulate.Amount;
        this.Msisdn = simulate.Msisdn;
        this.BillRefNumber = simulate.BillRefNumber;
        this.Production = simulate.Production;
    }

    public static class Simulate {

        private String ShortCode;
        private String CommandID;
        private String Amount;
        private String Msisdn;
        private String BillRefNumber;
        private int Production;


        public Simulate(String shortCode, String commandID, String amount, String msisdn,String BillRefNumber ) {
            this.ShortCode = shortCode;
            this.CommandID = commandID;
            this.Amount = amount;
            this.Msisdn = Util.sanitizePhoneNumber(msisdn);
            this.BillRefNumber = BillRefNumber;

        }

        public Simulate setCommandID(String commandID) {
            this.CommandID = commandID;
            return this;
        }

        public Simulate setBillRefNumber(String billRefNumber) {
            this.BillRefNumber = billRefNumber;
            return this;
        }

        public C2BTransact build() {
            return new C2BTransact(this);
        }
    }

    public String getShortCode() {
        return ShortCode;
    }

    public String getCommandID() {
        return CommandID;
    }

    public String getAmount() {
        return Amount;
    }

    public String getMsisdn() {
        return Msisdn;
    }

    public String getBillRefNumber() {
        return BillRefNumber;
    }

    public int getProduction() {
        return Production;
    }
}
