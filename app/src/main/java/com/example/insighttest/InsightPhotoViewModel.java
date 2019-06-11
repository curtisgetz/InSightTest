package com.example.insighttest;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Duration;

import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InsightPhotoViewModel extends ViewModel {

/*    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat mInsightDateFormat = new SimpleDateFormat("yyy-MM-dd");*/

    private MutableLiveData<InsightResponse> mLiveInsightResponse = new MutableLiveData<>();
    private InsightPhotoEndpoint mApiService;
    private final static String SOL_QUERY_APPEND = ":sol";
    private final static String MISSION_QUERY_APPEND = "insight:mission";
    private int mMaxSolEstimate;
    private int mCurrentSol = 0;

    private final DateTime mInsightSolZero = new DateTime(DateTime.parse("2018-11-26T00:00:00.000Z"));
    private DateTime mCurrentDate = new DateTime(DateTimeZone.UTC);


    public InsightPhotoViewModel() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://mars.nasa.gov/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mApiService = retrofit.create(InsightPhotoEndpoint.class);
        mMaxSolEstimate = estimateMaxSol();

        Log.d("LOG", "ViewModel created");
    }

    public void searchBySol(int sol){
        String solSearch = sol + SOL_QUERY_APPEND;
        Call<InsightResponse> insightCall = mApiService.getPhotosBySol(MISSION_QUERY_APPEND, solSearch);
        insightCall.enqueue(new Callback<InsightResponse>() {
            @Override
            public void onResponse(Call<InsightResponse> call, Response<InsightResponse> response) {
                if(response.body() != null){
                    Log.d("LOG", response.toString());
                    InsightResponse insightResponse = response.body();

                    mLiveInsightResponse.postValue(insightResponse);


                }else {
                    Log.d("LOG", "Response Null");
                }
            }

            @Override
            public void onFailure(Call<InsightResponse> call, Throwable t) {
                Log.e("LOG", t.getMessage());
            }
        });
    }

    public void searchPrevSol(){
        if(mCurrentSol > 0) {
            mCurrentSol--;
        }
        searchBySol(mCurrentSol);
    }

    public void searchNextSol(){
        mCurrentSol++;
        searchBySol(mCurrentSol);
    }

    public LiveData<InsightResponse> getInsightResponse(){
        return mLiveInsightResponse;
    }

    public int getCurrentSol(){
        return mCurrentSol;
    }
    /**
     * Get a rough estimation of Max Sol
     * @return estimated max sol
     */
    public int estimateMaxSol(){
        Duration duration = new Duration(mInsightSolZero, mCurrentDate);

        Days numberOfDays = duration.toStandardDays();
        Log.d("LOG", String.valueOf(numberOfDays.getDays()));
        return numberOfDays.getDays();
    }

}
