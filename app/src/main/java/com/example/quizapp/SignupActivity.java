package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.db.DatabaseHelper;
import com.example.quizapp.models.User;

/**
 * SignupActivity handles the user registration process.
 * It takes username and password input, validates it, and stores it in the SQLite database.
 */
public class SignupActivity extends AppCompatActivity {

    // UI Components
    private EditText etUsername, etPassword, etConfirmPassword;
    private Button btnSignup;
    private TextView tvLoginLink;
    
    // Database Helper for user operations
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup); // Set the XML layout

        // Initialize Database Helper
        databaseHelper = new DatabaseHelper(this);

        // Link UI components with XML IDs
        etUsername = findViewById(R.id.et_signup_username);
        etPassword = findViewById(R.id.et_signup_password);
        etConfirmPassword = findViewById(R.id.et_signup_confirm_password);
        btnSignup = findViewById(R.id.btn_signup);
        tvLoginLink = findViewById(R.id.tv_login_link);

        // Handle Signup Button Click
        btnSignup.setOnClickListener(v -> {
            // Get input values
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            // Validate inputs
            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                // Check if user already exists in the database
                if (databaseHelper.checkUserExists(username)) {
                    Toast.makeText(SignupActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                } else {
                    // Create new user and add to database
                    User newUser = new User(username, password);
                    long res = databaseHelper.addUser(newUser);
                    
                    if (res > 0) {
                        Toast.makeText(SignupActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        // Redirect to Login Screen
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // Close SignupActivity so user can't go back to it
                    } else {
                        Toast.makeText(SignupActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Handle "Already have an account? Login" link click
        tvLoginLink.setOnClickListener(v -> {
            finish(); // Simply close this activity to return to the previous one (Login)
        });
    }
}
