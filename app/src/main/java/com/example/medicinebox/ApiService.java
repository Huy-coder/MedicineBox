package com.example.medicinebox;

import retrofit2.Call;
import retrofit2.http.GET;

    public interface ApiService {
        @GET("state") // Replace with your server's endpoint
        Call<MedicineDataModel> getJsonData();
    }
