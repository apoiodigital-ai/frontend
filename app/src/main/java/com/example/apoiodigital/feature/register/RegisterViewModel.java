package com.example.apoiodigital.feature.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.apoiodigital.core.tables.usuario.Usuario;

public class RegisterViewModel extends ViewModel {

    private RegisterRepository repository;

    public RegisterViewModel() {
        this.repository = new RegisterRepository();
    }


    public LiveData<Boolean> registerUser(Usuario usuario) {
        return repository.createUser(usuario);
    }

}
