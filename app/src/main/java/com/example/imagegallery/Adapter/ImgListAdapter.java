package com.example.imagegallery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.imagegallery.Model.PhotoKeyDetailsModel;
import com.example.imagegallery.R;

import java.util.List;

public class ImgListAdapter extends RecyclerView.Adapter<ImgListAdapter.ViewHolder> {

    Context context;
    List<PhotoKeyDetailsModel> list;
    protected PhotoListener photoListener;


    public ImgListAdapter(Context context, List<PhotoKeyDetailsModel> list, PhotoListener photoListener) {
        this.context = context;
        this.list = list;
        this.photoListener = photoListener;
    }

    public void setImageList(List<PhotoKeyDetailsModel> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImgListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.image_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImgListAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getUrlS()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoListener.onPhotoClick(list.get(position).getUrlS());
            }
        });

    }

    @Override
    public int getItemCount() {

        return this.list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.galleryImages);
        }
    }

    public interface PhotoListener {
        void onPhotoClick(String path);
    }
}
