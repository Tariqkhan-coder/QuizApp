package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    private TextView tvFinalScore;
    private Button btnRetry, btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvFinalScore = findViewById(R.id.tv_final_score);
        btnRetry = findViewById(R.id.btn_retry);
        btnHome = findViewById(R.id.btn_home);

        int score = getIntent().getIntExtra("SCORE", 0);
        int totalQuestions = getIntent().getIntExtra("TOTAL_QUESTIONS", 0);

        tvFinalScore.setText("Your Score: " + score + " / " + totalQuestions);

        btnRetry.setOnClickListener(v -> {
            // Restart the app from Categories to try again (or maybe just restart last
            // category?)
            // For simplicity, go to Categories
            Intent intent = new Intent(ResultActivity.this, CategoriesActivity.class);
            startActivity(intent);
            finish();
        });

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, CategoriesActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
