package com.example.apoiodigital.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.apoiodigital.R;
import com.example.apoiodigital.Utils.ConfigManager;
import com.example.apoiodigital.Utils.FontUtils;
import com.example.apoiodigital.Utils.SessionManager;
import com.example.apoiodigital.View.LauncherActivity;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        TextView textTitle = rootView.findViewById(R.id.textTitleSettings);
        AppCompatSpinner spinner = rootView.findViewById(R.id.spinnerConfig);
        Button btnLogOff = rootView.findViewById(R.id.logOffBtn);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.spinner_text,
                getResources().getStringArray(R.array.config_options)
        );

        adapter.setDropDownViewResource(R.layout.spinner_text);
        spinner.setAdapter(adapter);

        ConfigManager configManager = new ConfigManager(requireContext());

        String savedOption = configManager.getFontSizeOption();
        int position = adapter.getPosition(savedOption);
        if (position >= 0) {
            spinner.setSelection(position);
        }

        FontUtils.applyFontSize(requireContext(), rootView);

        spinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int pos, long id) {
                String option = parent.getItemAtPosition(pos).toString();
                configManager.setFontSizeOption(option);

                FontUtils.applyFontSize(requireContext(), rootView);
                if (option.equals("GRANDE")) {
                    textTitle.setPadding(10, 30, 10, 30);
                } else {
                    textTitle.setPadding(30, 30, 30, 30);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        SwitchCompat switchVoice = rootView.findViewById(R.id.switchOptResponseVoice);

        btnLogOff.setOnClickListener(v -> {
            androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Sair da conta")
                    .setMessage("Tem certeza que deseja sair da conta?")
                    .setPositiveButton("Sim", (d, which) -> {
                        Toast.makeText(requireContext(), "Você saiu da sua conta", Toast.LENGTH_SHORT).show();

                        SessionManager sessionManager = new SessionManager(requireContext());
                        sessionManager.clear();
                        configManager.clear();

                        startActivity(new Intent(requireContext(), LauncherActivity.class));
                        requireActivity().finish();
                    })
                    .setNegativeButton("Não", (d, which) -> {
                        d.dismiss();
                        Toast.makeText(requireContext(), "Logout cancelado", Toast.LENGTH_SHORT).show();
                    })
                    .create();

            dialog.show();

            dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(getResources().getColor(R.color.LightBlueAD, null));
            dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(getResources().getColor(R.color.LightBlueAD, null));
        });

        return rootView;
    }
}
