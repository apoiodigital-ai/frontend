package com.example.apoiodigital.feature.screen_question;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ScreenQuestionApiService {

    @POST("resposta/validar/resposta-necessidade")
    Call<UserAnswerValidatorResponseDTO> validarRespostaDaNecessidade(
            @Body UserAnswerValidatorRequestDTO request
    );

}
