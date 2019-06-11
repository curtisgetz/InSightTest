package com.example.insighttest;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;


import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class InsightPhotoViewModel extends ViewModel {

    private final static long SOL_IN_MILLISECONDS = 88775244L;
    private final static DateTime mInsightSolZero = new DateTime(DateTime.parse("2018-11-26T00:00:00.000Z"));
    private final static int MAX_QUERY = 25;
    private static int mCurrentQuery = 0;

    private MutableLiveData<InsightResponse> mLiveInsightResponse = new MutableLiveData<>();
    private InsightRepository mRepository;
    private int mMaxSolEstimate;
    private int mCurrentSol = 0;


    private DateTime mCurrentDate = new DateTime(DateTimeZone.UTC);


    public InsightPhotoViewModel() {
        mRepository = InsightRepository.getInstance();
        mMaxSolEstimate = estimateMaxSol();
        Log.d("LOG", "ViewModel created");
    }

    public void searchFromInput(int sol){
        mCurrentQuery = 0;
        searchBySol(sol);
    }

    private void searchBySol(int sol){
        mCurrentSol = validateSolInRange(sol);
        mRepository.getPhotosSingle(mCurrentSol)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSingleObserver());
    }

    private SingleObserver<InsightResponse> getSingleObserver() {
        return new SingleObserver<InsightResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(InsightResponse insightResponse) {
                Log.d("LOG", "Success");
                checkIfActive(insightResponse);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("LOG", e.getMessage());
            }
        };
    }

    private void checkIfActive(InsightResponse insightResponse){
        Log.d("LOG", "Query Number = " + mCurrentQuery);
        if(insightResponse.getItems().isEmpty()){
            if(mCurrentQuery < MAX_QUERY && mCurrentSol < mMaxSolEstimate && mCurrentSol >=0){
                mCurrentSol++;
                mCurrentQuery++;
                Log.d("LOG", "Trying new sol " + mCurrentSol);
                searchBySol(mCurrentSol);
            }
        }else {
            mCurrentQuery = 0;
            mLiveInsightResponse.postValue(insightResponse);
        }
    }


    private int validateSolInRange(int sol){
        if(sol < 0){
            return 0;
        }else if(sol > mMaxSolEstimate){
            return mMaxSolEstimate;
        }else {
            return sol;
        }
    }

    public void searchPrevSol(){
        if(mCurrentSol > 0) {
            mCurrentSol--;
        }
        searchFromInput(mCurrentSol);
    }

    public void searchNextSol(){
        if(mCurrentSol > mMaxSolEstimate){
            mCurrentSol = mMaxSolEstimate;
        }else {
            mCurrentSol++;
        }
        searchFromInput(mCurrentSol);
    }

    public int getMaxSolEstimate(){
        return mMaxSolEstimate;
    }

    public LiveData<InsightResponse> getInsightResponse(){
        return mLiveInsightResponse;
    }

    public int getCurrentSol(){
        return mCurrentSol;
    }

    /**
     * Get an estimation of Max Sol
     * @return estimated max sol
     */
    public int estimateMaxSol(){
        Duration duration = new Duration(mInsightSolZero, mCurrentDate);
        long milliSinceLanding = duration.getMillis();
        int estimatedSols = (int) (milliSinceLanding / SOL_IN_MILLISECONDS);
        Log.d("LOG", String.valueOf(estimatedSols));
        return estimatedSols;
    }

}
