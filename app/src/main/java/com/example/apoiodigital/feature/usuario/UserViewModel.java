package com.example.apoiodigital.feature.usuario;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apoiodigital.feature.auth.data.TokenResponseDTO;
import com.example.apoiodigital.feature.usuario.data.UserIDDTO;
import com.example.apoiodigital.feature.usuario.data.Usuario;
import com.example.apoiodigital.feature.usuario.repository.UserRepository;

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

    public LiveData<Boolean> registerUser(Usuario usuario) {
        return repository.createUser(usuario);
    }

    public  LiveData<TokenResponseDTO> autenticar(String telefone, String senha){
        return repository.loginuser(telefone, senha);
    }

    public LiveData<UserIDDTO> getIdByToken(String token){
        return repository.getUserIDByToken(token);
    }



}



