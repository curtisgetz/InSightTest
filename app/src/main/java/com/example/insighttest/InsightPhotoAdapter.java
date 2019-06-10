package com.example.insighttest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InsightPhotoAdapter extends RecyclerView.Adapter {

    private List<InsightPhoto> mPhotos;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_item, parent, false);
        return new InsightPhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String photoUrl = mPhotos.get(position).getUrl();
        String title = mPhotos.get(position).getTitle();
        if(!photoUrl.isEmpty()){
            Picasso.get().load(photoUrl).into(((InsightPhotoViewHolder) holder).mPhotoIv);
            ((InsightPhotoViewHolder) holder).mTitleText.setText(title);
        }

    }

    @Override
    public int getItemCount() {
        return mPhotos == null ? 0 : mPhotos.size();
    }

    public void setData(List<InsightPhoto> photos){
        mPhotos = new ArrayList<>(photos);
        notifyDataSetChanged();
    }

    public void clearData(){
        mPhotos = new ArrayList<>();
        notifyDataSetChanged();
    }


    class InsightPhotoViewHolder extends RecyclerView.ViewHolder{


        @BindView(R.id.imageview)
        ImageView mPhotoIv;
        @BindView(R.id.photo_title)
        TextView mTitleText;

        public InsightPhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
