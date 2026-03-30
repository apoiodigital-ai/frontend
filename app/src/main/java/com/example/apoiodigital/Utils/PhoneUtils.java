package com.example.apoiodigital.Utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class PhoneUtils {
    public static void applyPhoneMask(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void afterTextChanged(Editable s) {
                if (isUpdating) return; // evita loop

                String tel = s.toString().replaceAll("[^\\d]", ""); // só números
                String formatted = "";

                if (tel.length() > 0) {
                    if (tel.length() <= 2) {
                        formatted = "(" + tel;
                    } else if (tel.length() <= 7) {
                        formatted = "(" + tel.substring(0, 2) + ") " + tel.substring(2);
                    } else if (tel.length() <= 11) {
                        formatted = "(" + tel.substring(0, 2) + ") "
                                + tel.substring(2, 7) + "-" + tel.substring(7);
                    } else {
                        formatted = "(" + tel.substring(0, 2) + ") "
                                + tel.substring(2, 7) + "-" + tel.substring(7, 11);
                    }
                }

                isUpdating = true;
                s.replace(0, s.length(), formatted);
                isUpdating = false;

                editText.setSelection(formatted.length());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });
    }

    public static String getDigits(String s) {
        if (s == null) return "";
        return s.replaceAll("[^\\d]", "");
    }
}
