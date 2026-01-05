package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.db.DatabaseHelper;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etUsername;
    private Button btnCheck;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etUsername = findViewById(R.id.et_forgot_username);
        btnCheck = findViewById(R.id.btn_forgot_check);
        databaseHelper = new DatabaseHelper(this);

        btnCheck.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();

            if (username.isEmpty()) {
                Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show();
            } else {
                if (databaseHelper.checkUserExists(username)) {
                    // Navigate to Reset Password Activity
                    Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                    intent.putExtra("USERNAME", username);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
