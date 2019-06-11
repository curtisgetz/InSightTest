package com.example.insighttest;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class InsightRepository {

    private final static String SOL_QUERY_APPEND = ":sol";
    private final static String MISSION_QUERY_APPEND = "insight:mission";

    private static InsightRepository sInstance;
    private InsightPhotoEndpoint mApiService;

    public static InsightRepository getInstance(){
        if(sInstance == null){
            sInstance = new InsightRepository();
        }
        return sInstance;
    }

    private  InsightRepository(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://mars.nasa.gov/api/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mApiService = retrofit.create(InsightPhotoEndpoint.class);
    }


    public Single<InsightResponse> getPhotosSingle(int sol){
        String solSearch = sol + SOL_QUERY_APPEND;
        return mApiService.getPhotosBySolSingle(MISSION_QUERY_APPEND, solSearch);
    }



}
