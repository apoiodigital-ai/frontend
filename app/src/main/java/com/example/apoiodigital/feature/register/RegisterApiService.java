package com.example.apoiodigital.feature.register;

import com.example.apoiodigital.core.tables.usuario.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegisterApiService {

    @POST("usuario/salvar")
    Call<Usuario> criarConta(@Body Usuario usuario);


}
