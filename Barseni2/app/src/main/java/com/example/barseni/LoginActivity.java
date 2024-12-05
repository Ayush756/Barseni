package com.example.barseni;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginActivity extends AppCompatActivity {

    private EditText phoneEditText, passwordEditText;
    private Button loginButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Firestore and Auth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Bind views
        phoneEditText = findViewById(R.id.editTextPhone);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.btnLogin);

        // Set up button listener
        loginButton.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String phone = phoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch user data from Firestore
        db.collection("users")
                .document(phone) // Using phone number as the document ID
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String storedPassword = document.getString("password");
                            if (storedPassword != null && storedPassword.equals(password)) {
                                // Login successful
                                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "No user found with this phone number", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
