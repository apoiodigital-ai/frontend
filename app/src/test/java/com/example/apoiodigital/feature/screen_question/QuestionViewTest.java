package com.example.apoiodigital.feature.screen_question;

import android.content.Context;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.apoiodigital.databinding.QuestionLayoutBinding;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
@Config(sdk = 33)
public class QuestionViewTest {

    private Context context;
    private QuestionView questionView;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        // Since QuestionView might require Material theme for inflation
        context.setTheme(com.google.android.material.R.style.Theme_MaterialComponents_DayNight);
        questionView = new QuestionView(context);
    }

    @Test
    public void testQuestionViewWithNoOptionsInCarrossel() {
        assertNotNull(questionView);
        
        ViewGroup.LayoutParams params = questionView.getLayoutParams();
        questionView.setLayoutParams(params);
        questionView.setQuestion("Para qual lugar deseja ir?");
        questionView.setCarrossel(new ArrayList<>());

        QuestionLayoutBinding binding = questionView.getBinding();

        assertNotNull(binding);

        assertEquals(ViewGroup.GONE, binding.firstButton.getVisibility());
        assertEquals(ViewGroup.GONE, binding.secondButton.getVisibility());

        assertEquals("Para qual lugar deseja ir?", binding.titleText.getText().toString());
        assertNotNull("LayoutParams should not be null", params);
        assertEquals(ConstraintLayout.LayoutParams.MATCH_PARENT, params.width);
        assertEquals(ConstraintLayout.LayoutParams.MATCH_PARENT, params.height);
    }

    @Test
    public void testQuestionViewWithOneOptionInCarrossel() {
        assertNotNull(questionView);

        ViewGroup.LayoutParams params = questionView.getLayoutParams();
        questionView.setLayoutParams(params);
        questionView.setQuestion("Para qual lugar deseja ir?");

        List<String> opcoes = new ArrayList<>();
        opcoes.add("Opcao1");

        questionView.setCarrossel(opcoes);

        QuestionLayoutBinding binding = questionView.getBinding();

        assertNotNull(binding);

        assertEquals(ViewGroup.VISIBLE, binding.firstButton.getVisibility());
        assertEquals(ViewGroup.INVISIBLE, binding.secondButton.getVisibility());

        assertEquals("Opcao1", binding.firstButton.getText().toString());

        assertEquals("Para qual lugar deseja ir?", binding.titleText.getText().toString());
        assertNotNull("LayoutParams should not be null", params);
        assertEquals(ConstraintLayout.LayoutParams.MATCH_PARENT, params.width);
        assertEquals(ConstraintLayout.LayoutParams.MATCH_PARENT, params.height);
    }

    @Test
    public void testQuestionViewWithTwoOptionsInCarrossel() {
        assertNotNull(questionView);

        ViewGroup.LayoutParams params = questionView.getLayoutParams();
        questionView.setLayoutParams(params);
        questionView.setQuestion("Para qual lugar deseja ir?");

        List<String> opcoes = new ArrayList<>();
        opcoes.add("Opcao1");
        opcoes.add("Opcao2");

        questionView.setCarrossel(opcoes);

        QuestionLayoutBinding binding = questionView.getBinding();

        assertNotNull(binding);

        assertEquals(ViewGroup.VISIBLE, binding.firstButton.getVisibility());
        assertEquals(ViewGroup.VISIBLE, binding.secondButton.getVisibility());

        assertEquals("Opcao1", binding.firstButton.getText().toString());
        assertEquals("Opcao2", binding.secondButton.getText().toString());

        assertEquals("Para qual lugar deseja ir?", binding.titleText.getText().toString());
        assertNotNull("LayoutParams should not be null", params);
        assertEquals(ConstraintLayout.LayoutParams.MATCH_PARENT, params.width);
        assertEquals(ConstraintLayout.LayoutParams.MATCH_PARENT, params.height);
    }
}
