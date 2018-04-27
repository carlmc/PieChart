package com.example.cmunayll.chartjson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cmunayll on 11/04/2018.
 */

public class Gasto {

    @SerializedName("type")
    private String type;
    @SerializedName("subtype")
    private String subtype;
    @SerializedName("amount")
    private String amount;
    @SerializedName("date")
    private String date;

    public Gasto(String type, String subtype, String amount, String date) {
        this.type = type;
        this.subtype = subtype;
        this.amount = amount;
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public String getSubtype() {
        return subtype;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
}
