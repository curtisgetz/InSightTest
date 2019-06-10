package com.example.insighttest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new InsightPhotoAdapter();
        mRecyclerView.setAdapter(mAdapter);

    }

    @OnClick(R.id.search_button)
    public void onSearchClick(){
       /* Retrofit retrofit = new Retrofit.Builder().baseUrl("https://mars.nasa.gov/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        InsightPhotoEndpoint insightApi = retrofit.create(InsightPhotoEndpoint.class);

        Call<InsightResponse> call = insightApi.listPhotos();
        call.enqueue(new Callback<InsightResponse>() {
            @Override
            public void onResponse(Call<InsightResponse> call, Response<InsightResponse> response) {
                if(response.body() == null){
                    showFailure();
                }else {
                    InsightResponse insightResponse = response.body();
                    InsightPhoto insightPhoto = insightResponse.getItems().get(0);
                    List<InsightPhoto> photoList = insightResponse.getItems();
                    mAdapter.setData(photoList);
                    String url = insightPhoto.getUrl();
                    String title = insightPhoto.getTitle();
                    //showResponse(title);
                }
            }

            @Override
            public void onFailure(Call<InsightResponse> call, Throwable t) {
                showFailure();
            }
        });*/

       InsightRepository repository = InsightRepository.getInstance();
       String solInput = mSolEditText.getText().toString();
       if(solInput.isEmpty()){
           return;
       }
       int sol = Integer.valueOf(solInput);
       LiveData<InsightResponse> responseLiveData = repository.getPhotosBySol(sol);
       responseLiveData.observe(this, new Observer<InsightResponse>() {
           @Override
           public void onChanged(InsightResponse insightResponse) {
               Log.d("LOG", "onChanged");
               if(insightResponse != null){
                   if(insightResponse.getErrorMessage() != null){
                       showFailure("No Photos");
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

    public void showResponse(String url){
        Toast.makeText(this, url, Toast.LENGTH_LONG).show();
    }

    public void showFailure(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
