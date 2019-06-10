package com.example.insighttest;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InsightRepository {


    private static InsightRepository sInstance;
    private InsightPhotoEndpoint mApiService;
    private final static String SOL_QUERY_APPEND = ":sol";
    private final static String MISSION_QUERY_APPEND = "insight:mission";

    public static InsightRepository getInstance(){
        if(sInstance == null){
            sInstance = new InsightRepository();
        }
        return sInstance;
    }

    private InsightRepository(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://mars.nasa.gov/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        mApiService = retrofit.create(InsightPhotoEndpoint.class);
    }


    public LiveData<InsightResponse> getPhotosBySol(int sol){


        String solSearch = sol + SOL_QUERY_APPEND;
        MutableLiveData<InsightResponse> responseMutableLiveData = new MutableLiveData<>();
        Call<InsightResponse> insightCall = mApiService.getPhotosBySol(MISSION_QUERY_APPEND, solSearch);
        insightCall.enqueue(new Callback<InsightResponse>() {
            @Override
            public void onResponse(Call<InsightResponse> call, Response<InsightResponse> response) {
                if(response.body() != null){
                    Log.d("LOG", response.toString());
                    InsightResponse insightResponse = response.body();

                    responseMutableLiveData.postValue(insightResponse);


                }else {
                    Log.d("LOG", "Response Null");
                }
            }

            @Override
            public void onFailure(Call<InsightResponse> call, Throwable t) {
                Log.e("LOG", t.getMessage());
            }
        });

        return responseMutableLiveData;
    }


}
