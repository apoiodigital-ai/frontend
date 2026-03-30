package com.example.apoiodigital.Fragment;

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

import com.example.apoiodigital.Model.Requisicao;
import com.example.apoiodigital.Model.RequisicaoResponse;
import com.example.apoiodigital.R;
import com.example.apoiodigital.Utils.FontUtils;
import com.example.apoiodigital.View.ModalView;
import com.example.apoiodigital.View.TutorialView;
import com.example.apoiodigital.ViewModel.modal.ModalViewModel;
import com.example.apoiodigital.databinding.OverlayLayoutBinding;

public class HomeFragment extends Fragment {

    private final Context _context;
    private OverlayLayoutBinding binding;
    private View mainOverlay;
    private ModalViewModel viewModel;

    public HomeFragment(Context context) {
        _context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        FontUtils.applyFontSize(requireContext(), rootView);
        viewModel = new ViewModelProvider(requireActivity()).get(ModalViewModel.class);

        var btnStart = rootView.findViewById(R.id.btnStartModal);
        btnStart.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            var windowManager = (WindowManager) _context.getSystemService(WINDOW_SERVICE);
            WindowManager.LayoutParams params = getWindowParams();
            LayoutInflater layoutInflater = LayoutInflater.from(_context);

            mainOverlay = layoutInflater.inflate(R.layout.overlay_layout, null);
            windowManager.addView(mainOverlay, params);

            binding = OverlayLayoutBinding.bind(mainOverlay);
            View modalController = new ModalView(viewModel, _context, layoutInflater, binding.container);
            binding.container.addView(modalController);

            viewModel.getAtalhoResponse().observeForever(new Observer<Requisicao>() {
                @Override
                public void onChanged(Requisicao requisicao) {
                    afterRequisicaoSent(windowManager, inflater);
                }
            });

            viewModel.getRequisicaoResponse().observeForever(new Observer<RequisicaoResponse>() {
                @Override
                public void onChanged(RequisicaoResponse requisicaoResponse) {
                    afterRequisicaoSent(windowManager, inflater);
                }
            });
//            viewModel.getAtalhoResponse().observe(getViewLifecycleOwner(), this::afterRequisicaoSent());
//            viewModel.getRequisicaoResponse().observe(getViewLifecycleOwner(), this::afterRequisicaoSent);
        });

        return rootView;
    }

    private void afterRequisicaoSent(WindowManager windowManager, LayoutInflater inflater) {
//
//        var windowManager = (WindowManager) _context.getSystemService(WINDOW_SERVICE);
//        LayoutInflater inflater = LayoutInflater.from(_context);

        try {
            Log.e("FOWAOFWKAKFKWFW", "afterRequisicaoSent: AQUI NO COMECO DO TRY" );
            windowManager.removeView(mainOverlay);
            Log.e("FOWAOFWKAKFKWFW", "afterRequisicaoSent: AQUI NO COMECO DO TRY" );

        } catch (Exception ignoredEx) {}

        windowManager.addView(mainOverlay, getWindowParamsForTutorial());
        Log.e("FOWAOFWKAKFKWFW", "afterRequisicaoSent: ADICIONOU " );

        binding.container.removeAllViews();
        View tutorialController = new TutorialView(_context, inflater, binding.container, windowManager);
        binding.container.addView(tutorialController);
    }

    private WindowManager.LayoutParams getWindowParams() {
        return new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                getOverlayType(),
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
        );
    }

    private WindowManager.LayoutParams getWindowParamsForTutorial() {
        return new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                getOverlayType(),
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
    }

    private int getOverlayType() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                WindowManager.LayoutParams.TYPE_PHONE;
    }
}
