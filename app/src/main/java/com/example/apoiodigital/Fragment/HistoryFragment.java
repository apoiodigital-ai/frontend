package com.example.apoiodigital.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apoiodigital.R;
import com.example.apoiodigital.Adapter.RequestAdapter;
import com.example.apoiodigital.Dto.ListRequisicaoRequestDTO;
import com.example.apoiodigital.Dto.RequisicaoDTO;
import com.example.apoiodigital.Repository.HistoryRepository;
import com.example.apoiodigital.Utils.FontUtils;
import com.example.apoiodigital.Utils.NetworkUtil;
import com.example.apoiodigital.Utils.RequestItem;
import com.example.apoiodigital.Utils.RequisicaoUtils;
import com.example.apoiodigital.Utils.SessionManager;
import com.example.apoiodigital.ViewModel.HistoryViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HistoryFragment extends Fragment {

    private HistoryViewModel viewModel;
    private SessionManager sessionManager;
    private static final String TAG = "HistoryFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        ProgressBar progressBar = rootView.findViewById(R.id.progressBarHistory);
        progressBar.setVisibility(View.VISIBLE);


        sessionManager = new SessionManager(requireContext());

        TextView textViewMsg = rootView.findViewById(R.id.errorMsgTextVIew);
        textViewMsg.setVisibility(View.GONE);

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewRequest);

        HistoryRepository repository = new HistoryRepository(sessionManager);

        if (!NetworkUtil.isConnectedToInternet(requireContext())) {
            progressBar.setVisibility(View.GONE);
            textViewMsg.setVisibility(View.VISIBLE);
            textViewMsg.setText("Conecte-se à internet para carregar seu histórico");
            FontUtils.applyFontSize(requireContext(), textViewMsg);
        }
        else{
            viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
                @NonNull
                @Override
                public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                    return (T) new HistoryViewModel(repository);
                }
            }).get(HistoryViewModel.class);

            viewModel.getHistorico().observe(getViewLifecycleOwner(), r ->{
                progressBar.setVisibility(View.GONE);

                switch (r.status){
                    case SUCCESS:
                        exibirHistorico(r.getFullData(), recyclerView);
                        break;
                    case EMPTY:
                        textViewMsg.setVisibility(View.VISIBLE);
                        textViewMsg.setText("Sem histórico ainda, vamos começar!");
                        break;
                    case API_ERROR:
                        textViewMsg.setVisibility(View.VISIBLE);
                        textViewMsg.setText("Erro ao carregar histórico. Tente novamente mais tarde.");

                }
                FontUtils.applyFontSize(requireContext(), textViewMsg);
            });
        }



        return rootView;
    }

    private void exibirHistorico(ListRequisicaoRequestDTO req, RecyclerView recyclerView){


        Map<String, List<RequisicaoDTO>> lista = RequisicaoUtils.separarDatasAPI(req);
        List<RequestItem> itemsParaAdapter = new ArrayList<>();

        for(String key : Arrays.asList("hoje", "ontem", "estaSemana", "semanaPassada", "maisAntigo")){
            List<RequisicaoDTO> listaReq = lista.get(key);

            if(listaReq!= null && !listaReq.isEmpty()){

                itemsParaAdapter.add(RequestItem.createTitle(key));

                for(RequisicaoDTO item : listaReq){

                    itemsParaAdapter.add(RequestItem.createItem(item));
                }
            }
        }

        RequestAdapter requestAdapter = new RequestAdapter(itemsParaAdapter);
        recyclerView.setAdapter(requestAdapter);
    }
}
