package com.example.apoiodigital.feature.home;

import static android.content.Context.WINDOW_SERVICE;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.apoiodigital.core.tables.requisicao.Requisicao;
import com.example.apoiodigital.feature.modal.AskHelpActivity;
import com.example.apoiodigital.feature.modal.data.RequisicaoResponse;
import com.example.apoiodigital.R;
import com.example.apoiodigital.core.Utils.FontUtils;
import com.example.apoiodigital.feature.modal.ModalView;
import com.example.apoiodigital.feature.modal.viewmodel.AtalhoController;
import com.example.apoiodigital.feature.modal.viewmodel.RequisicaoController;
import com.example.apoiodigital.feature.tutorial.TutorialView;
import com.example.apoiodigital.feature.modal.viewmodel.AudioController;
import com.example.apoiodigital.databinding.OverlayLayoutBinding;

public class HomeFragment extends Fragment {

    private final Context _context;

    public HomeFragment(Context context) {
        _context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        FontUtils.applyFontSize(requireContext(), rootView);

        var btnStart = rootView.findViewById(R.id.btnStartModal);
        btnStart.setOnClickListener(v -> {

//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(new Intent(_context, AskHelpActivity.class));

        });

        return rootView;

    }

}
