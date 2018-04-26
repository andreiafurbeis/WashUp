package com.example.andrearubeis.wash_up;

import retrofit2.Retrofit;
import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {


    private static final String ROOT_URL = "";


    public RetroClient() {

    }

    /**
     * Get Retro Client
     *
     * @return JSON Object
     */
    private static Retrofit getRetroClient() {
        Globals g = Globals.getInstance();

        return new Retrofit.Builder()
                .baseUrl(g.getDomain()+ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiService getApiService() {
        return getRetroClient().create(ApiService.class);
    }
}
