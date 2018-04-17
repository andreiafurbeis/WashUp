package com.example.andrearubeis.wash_up;

import retrofit2.Retrofit;
import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {

    /**
     * Upload URL of your folder with php file name...
     * You will find this file in php_upload folder in this project
     * You can copy that folder and paste in your htdocs folder...
     */
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