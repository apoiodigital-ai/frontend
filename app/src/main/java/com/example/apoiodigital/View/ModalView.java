package com.example.apoiodigital.View;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.example.apoiodigital.Activity.ModalActivity;
import com.example.apoiodigital.Model.Atalho;
import com.example.apoiodigital.R;
import com.example.apoiodigital.Utils.SessionManager;
import com.example.apoiodigital.ViewModel.UserViewModel;
import com.example.apoiodigital.ViewModel.modal.ModalViewModel;
import com.example.apoiodigital.databinding.ModalLayoutBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModalView extends View {

    private final List<Atalho> atalhosCache = new ArrayList<>();
    private final ModalViewModel viewModel;
    private final UserViewModel userViewModel = new UserViewModel();

    private SessionManager sessionManager;
    private Context context;
    private ModalActivity modalActivity;
    private ModalLayoutBinding binding;

    private String userID = null;

    public ModalView(ModalViewModel viewModel, Context context, LayoutInflater inflater, ViewGroup root) {
        super(context);
        this.viewModel = viewModel;
        this.context = context;

        this.sessionManager = new SessionManager(context);
        this.modalActivity = new ModalActivity(context);

        init(inflater, root);
    }

    private void init(LayoutInflater inflater, ViewGroup root) {
        var view = inflater.inflate(R.layout.modal_layout, root, true);
        binding = ModalLayoutBinding.bind(view);

        carregarUserID();
    }

    private void carregarUserID() {
        String token = sessionManager.getAccessToken();

        if (token == null || token.isEmpty()) {
            Log.e("ModalView", "Token está vazio. Não é possível obter o userID.");
            return;
        }

        userViewModel.getIdByToken(token).observeForever(userIDDTO -> {

            if (userIDDTO == null || userIDDTO.getUserID() == null) {
                Log.e("ModalView", "Erro: API retornou userIDDTO nulo!");
                return;
            }

            userID = userIDDTO.getUserID().toString();

            viewModel.carregarAtalhos(userID);
            setViewObservers();
            setModalSettings(userID);
        });
    }

    public void setViewObservers() {

        viewModel.getRequisicaoSendLoading().observeForever(isLoading -> {
            if (isLoading) {
                binding.modalSubLayout.setVisibility(View.INVISIBLE);
                binding.modalLoading.setVisibility(View.VISIBLE);
                binding.voicePromptInput.setVisibility(View.INVISIBLE);
            } else {
                binding.modalLoading.setVisibility(View.INVISIBLE);
                binding.modalSubLayout.setVisibility(View.VISIBLE);
                binding.voicePromptInput.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getIsAtalhosLoading().observeForever(isLoading -> {
            if (isLoading) {
                binding.sugestoesRapidasSubLayout.setVisibility(View.INVISIBLE);
                binding.sugestoesRapidasLoading.setVisibility(View.VISIBLE);
            } else {
                binding.sugestoesRapidasLoading.setVisibility(View.INVISIBLE);
                binding.sugestoesRapidasSubLayout.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getAtalhoResponse().observeForever(requisicao -> {
            viewModel.getRequisicaoSendLoading().postValue(false);

            Intent i = new Intent("com.example.apoiodigital.SEND_TO_IA");
            i.putExtra("prompt", requisicao.getPrompt());
            i.putExtra("id_requisicao", requisicao.getId());

            context.sendBroadcast(i);
        });

        viewModel.getRequisicaoResponse().observeForever(resp -> {
            Log.e("VIEWMODELMODAL", "setViewObservers: " + resp.getId() );
            Intent i = new Intent("com.example.apoiodigital.SEND_TO_IA");
            i.putExtra("prompt", resp.getPrompt());
            i.putExtra("id_requisicao", resp.getId());
            context.sendBroadcast(i);
        });

        viewModel.getAtalhos().observeForever(atalhos -> {
            viewModel.getIsAtalhosLoading().postValue(false);

            if (atalhos.size() >= 3) {
                binding.sugestoesRapidasBtn.setText(atalhos.get(0).getTitulo());
                binding.sugestoesRapidasBtn2.setText(atalhos.get(1).getTitulo());
                binding.sugestoesRapidasBtn3.setText(atalhos.get(2).getTitulo());
            }

            atalhosCache.clear();
            atalhosCache.addAll(atalhos);
        });

        viewModel.getSttReturn().observeForever(text ->
                binding.promptInput.setText(text)
        );
    }

    public void setModalSettings(String _userID) {

        if (viewModel == null || _userID == null) {
            Log.e("ModalView", "setModalSettings chamado sem userID carregado!");
            return;
        }

        var windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        modalActivity.closeModal(binding, windowManager, context);
        modalActivity.keyboardValidationActions(binding);

        List<Button> sugestoesRapidasBtn = Arrays.asList(
                binding.sugestoesRapidasBtn,
                binding.sugestoesRapidasBtn2,
                binding.sugestoesRapidasBtn3
        );

        modalActivity.setSugestoesRapidasBtn(sugestoesRapidasBtn, atalhosCache, viewModel);

        modalActivity.setSendInputBtn(binding.sendPromptBtn, binding.promptInput, _userID, viewModel);
        modalActivity.setVoiceInputBtn(binding.voicePromptInput, viewModel);
    }
}
