package com.example.prueba;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GroqApiService {
    @POST("chat/completions")
    Call<GroqResponse> createChatCompletion(
            @Header("Authorization") String authorization,
            @Header("Content-Type") String contentType,
            @Body GroqRequest request
    );
}
