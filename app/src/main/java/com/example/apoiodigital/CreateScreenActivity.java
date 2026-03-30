package com.example.apoiodigital;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.apoiodigital.Model.Atalho;
import com.example.apoiodigital.View.TutorialView;
import com.example.apoiodigital.ViewModel.modal.ModalViewModel;

import com.example.apoiodigital.databinding.OverlayLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class CreateScreenActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

    }
}