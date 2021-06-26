
package com.example.imagegallery.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImgModel {

    @SerializedName("photos")
    @Expose
    private PhotosModel photos;
    @SerializedName("stat")
    @Expose
    private String stat;

    public PhotosModel getPhotos() {
        return photos;
    }

    public void setPhotos(PhotosModel photos) {
        this.photos = photos;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

}
