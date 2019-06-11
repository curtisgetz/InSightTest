package com.example.insighttest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.photo_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.sol_to_search)
    EditText mSolEditText;

    private InsightPhotoAdapter mAdapter;
    private InsightPhotoViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new InsightPhotoAdapter();
        mRecyclerView.setAdapter(mAdapter);
        setupViewModel();
    }

    private void setupViewModel(){
        mViewModel = ViewModelProviders.of(this).get(InsightPhotoViewModel.class);
        mViewModel.getInsightResponse().observe(this, new Observer<InsightResponse>() {
            @Override
            public void onChanged(InsightResponse insightResponse) {
                Log.d("LOG", "onChanged");
                if(insightResponse != null){
                    if(insightResponse.getErrorMessage() != null){
                        String currentSol = String.valueOf(mViewModel.getCurrentSol());
                        showFailure("No Photos On Sol " + currentSol);
                    }else {
                        List<InsightPhoto> photoList = insightResponse.getItems();
                        mAdapter.setData(photoList);
                    }

                } else {
                    showFailure("Failure");
                }
            }
        });
    }

    @OnClick(R.id.search_button)
    public void onSearchClick(){
       String solInput = mSolEditText.getText().toString();
       if(solInput.isEmpty()){
           return;
       }
       int sol = Integer.valueOf(solInput);
       mViewModel.searchBySol(sol);

    }

    @OnClick(R.id.prev_sol)
    public void onPrevSolClick(){
        mViewModel.searchPrevSol();
    }

    @OnClick(R.id.next_sol)
    public void onNextSolClick(){
        mViewModel.searchNextSol();
    }

    public void showResponse(String url){
        Toast.makeText(this, url, Toast.LENGTH_LONG).show();
    }

    public void showFailure(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
