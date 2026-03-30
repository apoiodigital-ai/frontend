package com.example.apoiodigital.Service;

import com.example.apoiodigital.Dto.AccessTokenDTO;
import com.example.apoiodigital.Dto.IAResponseDTO;
import com.example.apoiodigital.Dto.ListRequisicaoRequestDTO;
import com.example.apoiodigital.Dto.RefreshRequestDTO;
import com.example.apoiodigital.Dto.TokenResponseDTO;
import com.example.apoiodigital.Dto.UserIDDTO;
import com.example.apoiodigital.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("usuario/salvar")
    Call<User> criarConta(@Body User usuario);

    @POST("auth/login")
    Call<TokenResponseDTO> autenticarUsuario(@Query("telefone") String telefone,
                                             @Query("senha") String senha);

    @POST("auth/refresh")
    Call<AccessTokenDTO> refresh(@Body RefreshRequestDTO request);

    @GET("requisicao/carregar")
    Call<ListRequisicaoRequestDTO> pegarHistoriocRequisicaoPorUsuario(@Query("token") String token);

    @GET("resposta/listar/{idReq}")
    Call<List<IAResponseDTO>> pegarIAResponsePorRequisicao(@Path("idReq")String idReq);

    @GET("usuario/me")
    Call<UserIDDTO> pegarUsuarioIdPorToken(@Query("token") String token);
}
