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



    private MutableLiveData<InsightResponse> mLiveInsightResponse = new MutableLiveData<>();
    private InsightRepository mRepository;
    private final static long SOL_IN_MILLISECONDS = 88775244L;
    private int mMaxSolEstimate;
    private int mCurrentSol = 0;

    private final DateTime mInsightSolZero = new DateTime(DateTime.parse("2018-11-26T00:00:00.000Z"));
    private DateTime mCurrentDate = new DateTime(DateTimeZone.UTC);


    public InsightPhotoViewModel() {
        mRepository = InsightRepository.getInstance();
        mMaxSolEstimate = estimateMaxSol();
        Log.d("LOG", "ViewModel created");
    }

    public void searchBySol(int sol){
        mCurrentSol = sol;
        mRepository.getPhotosSingle(sol)
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
                mLiveInsightResponse.postValue(insightResponse);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("LOG", e.getMessage());
            }
        };
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
