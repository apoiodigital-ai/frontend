package com.example.apoiodigital.Repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.apoiodigital.Dto.AccessTokenDTO;
import com.example.apoiodigital.Dto.IAResponseDTO;
import com.example.apoiodigital.Dto.ListRequisicaoRequestDTO;
import com.example.apoiodigital.Network.RetrofitClient;
import com.example.apoiodigital.Service.ApiService;
import com.example.apoiodigital.Utils.HistoricoResult;
import com.example.apoiodigital.Utils.SessionManager;

import java.net.UnknownHostException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryRepository {

    private final ApiService apiService;

    private final AuthRepository authRepository;
    private final SessionManager sessionManager;

    public HistoryRepository(SessionManager sessionManager) {
        this.apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        this.sessionManager = sessionManager;
        this.authRepository = new AuthRepository(sessionManager, apiService);
    }

    public LiveData<HistoricoResult> getHistorico() {
        MutableLiveData<HistoricoResult> liveData = new MutableLiveData<>();
        requestHistorico(liveData);
        return liveData;
    }

    private void requestHistorico(MutableLiveData<HistoricoResult> liveData) {
        String token = sessionManager.getAccessToken();

        apiService.pegarHistoriocRequisicaoPorUsuario(token)
                .enqueue(new Callback<ListRequisicaoRequestDTO>() {
                    @Override
                    public void onResponse(Call<ListRequisicaoRequestDTO> call, Response<ListRequisicaoRequestDTO> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ListRequisicaoRequestDTO data = response.body();
                            liveData.setValue(HistoricoResult.success(data));
                            Log.d("Token", "válido");
                        } else if (response.code() == 401 && isTokenExpired(response)) {
                            Log.d("Token", "token expirado, tentando refresh");
                            refreshTokenAndRetry(liveData);
                        } else {
                            Log.d("Token", "erro na API");
                            liveData.setValue(HistoricoResult.apiError());
                        }
                    }

                    @Override
                    public void onFailure(Call<ListRequisicaoRequestDTO> call, Throwable t) {
                        if (t instanceof UnknownHostException) {
                            liveData.setValue(HistoricoResult.networkError());
                        } else {
                            liveData.setValue(HistoricoResult.apiError());
                        }
                    }
                });
    }

    public LiveData<List<IAResponseDTO>> getIAResponse(String requestId){
        MutableLiveData<List<IAResponseDTO>> liveData = new MutableLiveData<>();
        requestIAResponse(liveData, requestId);
        return liveData;
    }

    private void requestIAResponse(MutableLiveData<List<IAResponseDTO>> liveData, String requestId){
        apiService.pegarIAResponsePorRequisicao(requestId).enqueue(new Callback<List<IAResponseDTO>>() {
            @Override
            public void onResponse(Call<List<IAResponseDTO>> call, Response<List<IAResponseDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(response.body());
                } else {
                    liveData.setValue(null);
                    Log.e("IAService", "Erro na resposta: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<IAResponseDTO>> call, Throwable t) {
                Log.e("IAService", "Falha ao carregar IAResponse", t);
                liveData.setValue(null);
            }
        });
    }

    private boolean isTokenExpired(Response<?> response) {
        try {
            return response.code() == 401;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void refreshTokenAndRetry(MutableLiveData<HistoricoResult> liveData) {

        authRepository.refreshToken(new Callback<AccessTokenDTO>() {
            @Override
            public void onResponse(Call<AccessTokenDTO> call, Response<AccessTokenDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sessionManager.saveTokens(response.body().getAccessToken(), sessionManager.getRefreshToken());
                    requestHistorico(liveData); // retry
                } else {
                    liveData.setValue(HistoricoResult.apiError());
                }
            }

            @Override
            public void onFailure(Call<AccessTokenDTO> call, Throwable t) {
                liveData.setValue(HistoricoResult.apiError());
            }
        });

    }
}


