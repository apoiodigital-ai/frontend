package com.example.apoiodigital.feature.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apoiodigital.core.tables.usuario.Usuario;

public class RegisterViewModel extends ViewModel {

    private RegisterRepository repository;

    private MutableLiveData<RegisterState> state = new MutableLiveData<>();

    public RegisterViewModel() {
        this.repository = new RegisterRepository();
    }

    public MutableLiveData<RegisterState> getState() {
        return state;
    }

    public void registerUser(Usuario usuario) {
        state.setValue(new RegisterState(true, null, false));
        repository.createUser(usuario, new RegisterRepository.RegisterCallBack() {
            @Override
            public void onSuccess() {
                state.setValue(new RegisterState(false, null, true));
            }

            @Override
            public void onError(String error) {
                state.setValue(new RegisterState(false, error, false));
            }
        });
    }

}
