package com.example.apoiodigital.Utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.apoiodigital.R;

public class FontUtils {

    public static void applyFontSize(Context context, View rootView) {
        if (rootView == null) return;

        ConfigManager configManager = new ConfigManager(context);
        String option = configManager.getFontSizeOption();

        traverseAndApply(rootView, option, context);
    }

    private static void traverseAndApply(View view, String option, Context context) {
        if (view instanceof TextView) {
            TextView tv = (TextView) view;

            if ("ignoreFontUtils".equals(tv.getTag())) {
                return;
            }

            Object tag = tv.getTag(R.id.original_text_size);
            float originalSize;
            if (tag == null) {
                originalSize = tv.getTextSize() / context.getResources().getDisplayMetrics().scaledDensity;
                tv.setTag(R.id.original_text_size, originalSize);
            }
            else {
                originalSize = (float) tag;
            }

            float finalSize = originalSize;
            switch (option) {
                case "Grande":
                    finalSize = originalSize + 10f;
                    break;
                case "Pequena":
                    finalSize = originalSize - 10f;
                    break;
                case "Média":
                default:
                    finalSize = originalSize;
                    break;
            }

            tv.setTextSize(finalSize);

        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                traverseAndApply(group.getChildAt(i), option, context);
            }
        }
    }

}
