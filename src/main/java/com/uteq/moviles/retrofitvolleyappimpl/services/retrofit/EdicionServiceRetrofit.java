package com.uteq.moviles.retrofitvolleyappimpl.services.retrofit;

import com.uteq.moviles.retrofitvolleyappimpl.models.Edicion;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EdicionServiceRetrofit {
    @GET("issues.php")
    public Call<List<Edicion>> findAllIssuesById(@Query("j_id") String id);
}
