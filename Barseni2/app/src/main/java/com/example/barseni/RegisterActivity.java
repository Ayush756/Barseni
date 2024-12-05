package com.example.barseni;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private EditText phoneEditText, passwordEditText;
    private Button registerButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Bind views
        phoneEditText = findViewById(R.id.editTextPhone);
        passwordEditText = findViewById(R.id.editTextPassword);
        registerButton = findViewById(R.id.btnRegister);

        // Set up button listener
        registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String phone = phoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new user document in Firestore
        db.collection("users")
                .document(phone) // Use phone number as the document ID
                .set(new User(phone, password))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static class User {
        private String phone;
        private String password;

        public User(String phone, String password) {
            this.phone = phone;
            this.password = password;
        }

        public String getPhone() {
            return phone;
        }

        public String getPassword() {
            return password;
        }
    }
}
