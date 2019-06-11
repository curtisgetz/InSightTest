package com.example.insighttest;


import android.util.Log;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class InsightRepository {

    private final static String SOL_QUERY_APPEND = ":sol";
    private final static String MISSION_QUERY_APPEND = "insight:mission";
    private final static String API_BASE_URL = "https://mars.nasa.gov/api/";

    private static InsightRepository sInstance;
    private InsightPhotoEndpoint mApiService;

    public static InsightRepository getInstance(){
        if(sInstance == null){
            sInstance = new InsightRepository();
        }
        return sInstance;
    }

    private  InsightRepository(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(API_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mApiService = retrofit.create(InsightPhotoEndpoint.class);
    }


    public Single<InsightResponse> getPhotosSingle(int sol){
        String solSearch = sol + SOL_QUERY_APPEND;
        Log.d("LOG", API_BASE_URL + solSearch);
        return mApiService.getPhotosBySolSingle(MISSION_QUERY_APPEND, solSearch);
    }




}
