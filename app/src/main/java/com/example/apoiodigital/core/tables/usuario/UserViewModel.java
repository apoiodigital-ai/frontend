package com.example.apoiodigital.core.tables.usuario;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apoiodigital.feature.auth.data.TokenResponseDTO;
import com.example.apoiodigital.feature.usuario.data.UserIDDTO;

public class UserViewModel extends ViewModel {

    private UserRepository repository;
    private MutableLiveData<Boolean> userCreated;

    public UserViewModel() {
        this.repository = UserRepository.getRepository();
        this.userCreated = new MutableLiveData<>();
    }

    public LiveData<Boolean> getUserCreated() {
        return userCreated;
    }

    public LiveData<UserIDDTO> getIdByToken(String token){
        return repository.getUserIDByToken(token);
    }



}



