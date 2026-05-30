package com.example.apoiodigital.Service;

import com.example.apoiodigital.Dto.AccessTokenDTO;
import com.example.apoiodigital.Dto.FindBestAnswerResponseDTO;
import com.example.apoiodigital.Dto.IAResponseDTO;
import com.example.apoiodigital.Dto.ListRequisicaoRequestDTO;
import com.example.apoiodigital.Dto.RefreshRequestDTO;
import com.example.apoiodigital.Dto.TokenResponseDTO;
import com.example.apoiodigital.Dto.UserAnswerValidatorRequestDTO;
import com.example.apoiodigital.Dto.UserAnswerValidatorResponseDTO;
import com.example.apoiodigital.Dto.UserIDDTO;
import com.example.apoiodigital.Model.Atalho;
import com.example.apoiodigital.Dto.ChecksInformationNeedsRequestDTO;
import com.example.apoiodigital.Dto.ChecksInformationNeedsResponseDTO;
import com.example.apoiodigital.Model.FindBestAnswerRequestDTO;
import com.example.apoiodigital.Model.Requisicao;
import com.example.apoiodigital.Model.RequisicaoInput;
import com.example.apoiodigital.Model.RequisicaoResponse;
import com.example.apoiodigital.Model.STTResponse;
import com.example.apoiodigital.Model.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // ── Auth / Usuário ────────────────────────────────────────────────────────

    @POST("usuario/salvar")
    Call<User> criarConta(@Body User usuario);

    @POST("auth/login")
    Call<TokenResponseDTO> autenticarUsuario(@Query("telefone") String telefone,
                                             @Query("senha") String senha);

    @POST("auth/refresh")
    Call<AccessTokenDTO> refresh(@Body RefreshRequestDTO request);

    @GET("usuario/me")
    Call<UserIDDTO> pegarUsuarioIdPorToken(@Query("token") String token);

    // ── Requisição ────────────────────────────────────────────────────────────

    @POST("requisicao/enviar")
    Call<RequisicaoResponse> enviarRequisicao(@Body RequisicaoInput requisicaoInput);

    @GET("requisicao/carregar")
    Call<ListRequisicaoRequestDTO> pegarHistoriocRequisicaoPorUsuario(@Query("token") String token);

    // ── Resposta / IA ─────────────────────────────────────────────────────────

    @POST("resposta/achar-resposta")
    Call<FindBestAnswerResponseDTO> exigirRespostaIA(@Body FindBestAnswerRequestDTO requestDTO);

    @GET("resposta/listar/{idReq}")
    Call<List<IAResponseDTO>> pegarIAResponsePorRequisicao(@Path("idReq") String idReq);

    @POST("/validar/necessidade-informacoes")
    Call<ChecksInformationNeedsResponseDTO> validarNecessidadeInformacoes(
            @Body ChecksInformationNeedsRequestDTO requestDTO);

    @POST("validar/respostra-necessidade")
    Call<UserAnswerValidatorResponseDTO> validarRespostaDaNecessidade(
            @Body UserAnswerValidatorRequestDTO request
    );

    // ── Atalho ────────────────────────────────────────────────────────────────

    @GET("atalho/carregar")
    Call<List<Atalho>> carregarAtalhos(@Query("id_usuario") String idUsuario);

    @POST("atalho/iniciar")
    Call<Requisicao> iniciarAtalho(@Query("id_atalho") String idAtalho);

    // ── STT (porta 5000 — use getSttRetrofitInstance) ─────────────────────────

    @Multipart
    @POST("stt")
    Call<STTResponse> stt(@Part MultipartBody.Part audio);
}
