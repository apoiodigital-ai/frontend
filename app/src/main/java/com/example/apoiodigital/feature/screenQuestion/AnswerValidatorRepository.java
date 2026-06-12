package com.example.apoiodigital.feature.screenQuestion;

import com.example.apoiodigital.core.Network.RetrofitClient;
import com.example.apoiodigital.feature.modal.service.ModalApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnswerValidatorRepository {

    private ScreenQuestionApiService apiService;

    public AnswerValidatorRepository() {
        this.apiService = RetrofitClient.getRetrofitInstance().create(ScreenQuestionApiService.class);
    }

    public void validarRespostaDaNecessidade(UserAnswerValidatorRequestDTO request, AnswerValidatorCallBack callback){
        apiService.validarRespostaDaNecessidade(request).enqueue(new Callback<UserAnswerValidatorResponseDTO>() {
            @Override
            public void onResponse(Call<UserAnswerValidatorResponseDTO> call, Response<UserAnswerValidatorResponseDTO> response) {
                if(response.isSuccessful() && response.body() != null){
                    callback.onSuccess(response.body());
                }else {
                    callback.onError("Erro ao validar resposta da necessidade");
                }
            };

            @Override
            public void onFailure(Call<UserAnswerValidatorResponseDTO> call, Throwable t) {
                callback.onError("Erro ao validar resposta da necessidade");
            };
        });
    }

    public interface AnswerValidatorCallBack{
        void onSuccess(UserAnswerValidatorResponseDTO response);
        void onError(String error);
    }

}
