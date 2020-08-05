package com.example.imasdb.model.RequestBodies;

import com.google.gson.annotations.SerializedName;

public class CreateListBody {
    @SerializedName("name")
    String name;
   @SerializedName("description")
    String desc;
   @SerializedName("language")
    String language;

    public CreateListBody(String name, String desc, String language) {
        this.name = name;
        this.desc = desc;
        this.language = language;
    }
}

