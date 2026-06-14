package com.example.apoiodigital.feature.screen_question;

import androidx.lifecycle.MutableLiveData;

public class AnswerValidatorController {

    private final AnswerValidatorRepository answerValidatorRepository;

    private MutableLiveData<UserAnswerValidatorResponseDTO> responseData = new MutableLiveData<>();
    private MutableLiveData<AnswerValidatorState> state = new MutableLiveData<>();

    public MutableLiveData<UserAnswerValidatorResponseDTO> getResponseData() {
        return responseData;
    }

    public MutableLiveData<AnswerValidatorState> getState() {
        return state;
    }

    public AnswerValidatorController(AnswerValidatorRepository answerValidatorRepository) {
        this.answerValidatorRepository = answerValidatorRepository;
    }

    public void validarRespostaDaNecessidade(UserAnswerValidatorRequestDTO request) {
        state.postValue(new AnswerValidatorState(true, null, false));
        answerValidatorRepository.validarRespostaDaNecessidade(request, new AnswerValidatorRepository.AnswerValidatorCallBack() {
            @Override
            public void onSuccess(UserAnswerValidatorResponseDTO response) {
                responseData.postValue(response);
                state.postValue(new AnswerValidatorState(false, null, true));
            }
            @Override
            public void onError(String error) {
                // Tratar erros
                state.postValue(new AnswerValidatorState(false, error, false));

            }
        });
    }
}
