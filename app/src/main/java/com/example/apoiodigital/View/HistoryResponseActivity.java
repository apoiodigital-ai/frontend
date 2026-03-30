package com.example.apoiodigital.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apoiodigital.R;
import com.example.apoiodigital.Adapter.HistoryResponseAdapter;
import com.example.apoiodigital.Dto.IAResponseDTO;
import com.example.apoiodigital.Repository.HistoryRepository;
import com.example.apoiodigital.Utils.FontUtils;
import com.example.apoiodigital.Utils.SessionManager;
import com.example.apoiodigital.ViewModel.HistoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class HistoryResponseActivity extends BaseActivity {

    private HistoryViewModel viewModel;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_response);
        recyclerView = findViewById(R.id.recyclerViewHistoryResponse);

        SessionManager sessionManager = new SessionManager(this);
        HistoryRepository repository = new HistoryRepository(sessionManager);

        TextView txt = findViewById(R.id.errorMsgTextView);
        txt.setVisibility(View.GONE);

        viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new HistoryViewModel(repository);
            }
        }).get(HistoryViewModel.class);

        viewModel.getIAResposeByRequestId(getIntent().getStringExtra("button_id"))
                .observe(this, iaResponseDTOS -> {

                    if (iaResponseDTOS == null) {
                        txt.setVisibility(View.VISIBLE);
                        FontUtils.applyFontSize(this, txt);
                        Toast.makeText(this, "Falha ao carregar respostas", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<IAResponseDTO> iaResponses = new ArrayList<>();

                    for (IAResponseDTO iaResponse : iaResponseDTOS) {
                        iaResponses.add(iaResponse);
                    }

                    HistoryResponseAdapter adapter = new HistoryResponseAdapter(
                            iaResponses,
                            this,
                            getIntent().getStringExtra("title_req")
                    );

                    recyclerView.setAdapter(adapter);
                    FontUtils.applyFontSize(this, recyclerView);
                });




//        iaResponses.add(new IAResponseDTO("saudacao", "Olá! Como posso ajudar?"));
//        iaResponses.add(new IAResponseDTO("erro_api", "Houve um problema ao acessar o servidor."));
//        iaResponses.add(new IAResponseDTO("ajuda", "Você pode verificar seu histórico ou criar uma nova requisição."));



        ImageButton btn = findViewById(R.id.backBtnA);
        btn.setOnClickListener(l ->{
            Intent intent = new Intent(HistoryResponseActivity.this, MainActivity.class);

            intent.putExtra("open_fragment", "history");

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            startActivity(intent);

            finish();
        });

    }


}
