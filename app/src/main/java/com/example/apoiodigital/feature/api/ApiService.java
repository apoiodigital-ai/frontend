package com.example.apoiodigital.feature.api;

import com.example.apoiodigital.feature.auth.data.AccessTokenDTO;
import com.example.apoiodigital.feature.tutorial.data.FindBestAnswerResponseDTO;
import com.example.apoiodigital.feature.history.data.IAResponseDTO;
import com.example.apoiodigital.feature.history.data.ListRequisicaoRequestDTO;
import com.example.apoiodigital.feature.auth.data.RefreshRequestDTO;
import com.example.apoiodigital.feature.auth.data.TokenResponseDTO;
import com.example.apoiodigital.feature.usuario.data.UserAnswerValidatorRequestDTO;
import com.example.apoiodigital.feature.usuario.data.UserAnswerValidatorResponseDTO;
import com.example.apoiodigital.feature.usuario.data.UserIDDTO;
import com.example.apoiodigital.core.tables.atalho.Atalho;
import com.example.apoiodigital.feature.tutorial.data.ChecksInformationNeedsRequestDTO;
import com.example.apoiodigital.feature.tutorial.data.ChecksInformationNeedsResponseDTO;
import com.example.apoiodigital.feature.tutorial.data.FindBestAnswerRequestDTO;
import com.example.apoiodigital.core.tables.requisicao.Requisicao;
import com.example.apoiodigital.feature.modal.data.RequisicaoInput;
import com.example.apoiodigital.feature.modal.data.RequisicaoResponse;
import com.example.apoiodigital.feature.modal.data.STTResponse;
import com.example.apoiodigital.core.tables.usuario.Usuario;

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
    Call<Usuario> criarConta(@Body Usuario usuario);

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
