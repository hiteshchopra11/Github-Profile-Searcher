package com.hiteshchopra.magnumhitesh.Models;

import com.google.gson.annotations.SerializedName;

public class Items {

    @SerializedName("login")
    public String loginName;
    @SerializedName("id")
    public Integer loginID;
    @SerializedName("avatar_url")
    public String imageUrl;


    public Integer getId() {
        return loginID;
    }

    public void setId(Integer loginID) {
        this.loginID = loginID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}