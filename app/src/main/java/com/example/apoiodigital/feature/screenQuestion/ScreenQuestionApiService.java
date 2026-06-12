package com.example.apoiodigital.feature.screenQuestion;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ScreenQuestionApiService {


    @POST("validar/resposta-necessidade")
    Call<UserAnswerValidatorResponseDTO> validarRespostaDaNecessidade(
            @Body UserAnswerValidatorRequestDTO request
    );

}
