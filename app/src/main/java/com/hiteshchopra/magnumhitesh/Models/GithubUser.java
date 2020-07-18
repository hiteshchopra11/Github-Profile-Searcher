package com.hiteshchopra.magnumhitesh.Models;

import android.util.Log;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GithubUser {

    @SerializedName("items")
    public List<Items> items = null;

    public List<Items> getItems() {
        if(items==null) {
            Log.e("ERROR","ERROR");
        }
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

}