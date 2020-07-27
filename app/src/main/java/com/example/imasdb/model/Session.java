package com.example.imasdb.model;

import com.google.gson.annotations.SerializedName;

public class Session {
    private boolean success;

    @SerializedName("session_id")
    private String sessionId;
}
