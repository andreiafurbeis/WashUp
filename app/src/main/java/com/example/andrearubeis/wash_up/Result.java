package com.example.andrearubeis.wash_up;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {


    @SerializedName("result")
    @Expose
    private String result;

    @SerializedName("value")
    @Expose
    private String value;


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return la risposta del server
     */
    public String getResult() {
        return result;
    }

    /**
     * @param result il risultato del caricamento
     */
    public void setResult(String result) {
        this.result = result;
    }

}
