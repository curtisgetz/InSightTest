package com.example.insighttest;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class InsightPhotoVMFactory extends ViewModelProvider.NewInstanceFactory {

    private int mSol;

    public InsightPhotoVMFactory(int sol) {
        mSol = sol;
    }


/*    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new InsightPhotoViewModel(mSol);
    }*/
}
