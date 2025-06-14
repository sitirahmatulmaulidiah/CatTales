package com.example.latihan_praktikum_7.data.network;

import com.example.latihan_praktikum_7.data.entity.Cat;
import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;

public interface ApiService {
    @GET("cats")
    Call<List<Cat>> getCats();
}
