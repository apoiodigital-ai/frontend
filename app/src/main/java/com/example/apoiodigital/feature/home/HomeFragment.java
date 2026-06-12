package com.example.apoiodigital.feature.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.apoiodigital.feature.modal.AskHelpActivity;
import com.example.apoiodigital.R;
import com.example.apoiodigital.core.Utils.FontUtils;

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
