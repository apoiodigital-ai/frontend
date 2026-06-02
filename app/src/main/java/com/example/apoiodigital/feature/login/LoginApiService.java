package com.example.apoiodigital.feature.login;

import com.example.apoiodigital.feature.auth.data.TokenResponseDTO;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginApiService {

    @POST("auth/login")
    Call<TokenResponseDTO> autenticarUsuario(@Query("telefone") String telefone,
                                             @Query("senha") String senha);


}
