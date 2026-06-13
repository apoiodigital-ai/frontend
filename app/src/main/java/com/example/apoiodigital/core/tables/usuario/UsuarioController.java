package com.example.apoiodigital.core.tables.usuario;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.apoiodigital.core.Utils.SessionManager;
import com.example.apoiodigital.feature.usuario.data.UserIDDTO;

public class UsuarioController {

    private UserRepository repository;
    private MutableLiveData<Boolean> userCreated;
    private MutableLiveData<UserIDDTO> userID;

    public LiveData<UserIDDTO> getUserID(){
        return userID;
    }


    public UsuarioController() {
        this.repository = UserRepository.getRepository();
        this.userCreated = new MutableLiveData<>();
        this.userID = new MutableLiveData<>();
    }

    public void getIdByToken(Context context){

        var sessionManager = new SessionManager(context);
        String token = sessionManager.getAccessToken();
        if(token == null || token.isEmpty()) return;

        repository.getUserIDByToken(token, new UserRepository.GetTokenCallBack() {
            @Override
            public void onSuccess(UserIDDTO response) {
                userID.setValue(response);

            }

            @Override
            public void onError(String error) {

            }
        });

    }





}



