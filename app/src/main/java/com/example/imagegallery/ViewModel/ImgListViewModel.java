package com.example.imagegallery.ViewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.imagegallery.Model.ImgModel;
import com.example.imagegallery.Model.PhotoKeyDetailsModel;
import com.example.imagegallery.Model.PhotosModel;
import com.example.imagegallery.Networking.FlickrApiService;
import com.example.imagegallery.Networking.RetrofitObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImgListViewModel extends ViewModel {

    private final MutableLiveData<List<PhotoKeyDetailsModel>> list;
    public ImgListViewModel() {
        this.list = new MutableLiveData<>();
    }

    public MutableLiveData<List<PhotoKeyDetailsModel>> getImageListObserver() {
        return this.list;
    }

    public void makeAPICall(){

        FlickrApiService api = RetrofitObject.getRetrofitClient().create(FlickrApiService.class);

        Call<ImgModel> call = api.getImages();

        call.enqueue(new Callback<ImgModel>() {
            @Override
            public void onResponse(Call<ImgModel> call, Response<ImgModel> response) {
                ImgModel data = response.body();
                PhotosModel photos = data.getPhotos();
                List<PhotoKeyDetailsModel> photo = photos.getPhoto();
                list.postValue(photo);
            }

            @Override
            public void onFailure(Call<ImgModel> call, Throwable t) {
                Log.d("TAG", t.getMessage());
            }
        });


    }
}
