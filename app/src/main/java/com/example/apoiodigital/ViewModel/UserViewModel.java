package com.example.apoiodigital.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apoiodigital.Dto.TokenResponseDTO;
import com.example.apoiodigital.Dto.UserIDDTO;
import com.example.apoiodigital.Model.User;
import com.example.apoiodigital.Repository.UserRepository;

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

    public LiveData<Boolean> registerUser(User user) {
        return repository.createUser(user);
    }

    public  LiveData<TokenResponseDTO> autenticar(String telefone, String senha){
        return repository.loginuser(telefone, senha);
    }

    public LiveData<UserIDDTO> getIdByToken(String token){
        return repository.getUserIDByToken(token);
    }



}



