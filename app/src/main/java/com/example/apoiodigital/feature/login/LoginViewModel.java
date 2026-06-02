package com.example.apoiodigital.feature.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apoiodigital.feature.auth.data.TokenResponseDTO;

public class LoginViewModel extends ViewModel {

    private LoginRepository repository;
    private final MutableLiveData<LoginState> loginState = new MutableLiveData<>();

    public LiveData<LoginState> getLoginState() {
        return loginState;
    }

    public LoginViewModel() {
        repository = new LoginRepository();
    }

    public LiveData<TokenResponseDTO> autenticar(String telefone, String senha){
        loginState.setValue(new LoginState(true, null, false));
        return repository.loginuser(telefone, senha, new LoginRepository.LoginCallBack() {
            @Override
            public void onSuccess() {
                loginState.setValue(new LoginState(false, null, true));
            }
            @Override
            public void onError(String error) {
                loginState.setValue(new LoginState(false, error, false));
            }
        });
    }
}
