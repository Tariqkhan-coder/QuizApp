package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.db.DatabaseHelper;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText etNewPassword, etConfirmPassword;
    private Button btnConfirm;
    private DatabaseHelper databaseHelper;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnConfirm = findViewById(R.id.btn_reset_confirm);
        databaseHelper = new DatabaseHelper(this);

        // Get username passed from ForgotPasswordActivity
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("USERNAME")) {
            username = intent.getStringExtra("USERNAME");
        }

        btnConfirm.setOnClickListener(v -> {
            String newPass = etNewPassword.getText().toString().trim();
            String confirmPass = etConfirmPassword.getText().toString().trim();

            if (newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            } else if (!newPass.equals(confirmPass)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                if (username != null) {
                    boolean isUpdated = databaseHelper.updatePassword(username, newPass);
                    if (isUpdated) {
                        Toast.makeText(this, "Password Updated Successfully", Toast.LENGTH_SHORT).show();
                        // Navigate back to Login
                        Intent loginIntent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                        // Clear stack so user can't go back to reset page
                        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(loginIntent);
                    } else {
                        Toast.makeText(this, "Error updating password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Error: User not identified", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
