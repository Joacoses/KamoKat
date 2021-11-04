package com.example.dabebel.registro;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dabebel.registro.databinding.ActivityMainBinding;
import com.example.dabebel.registro.databinding.EditarPerfilBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditarPerfil extends AppCompatActivity {

    private EditarPerfilBinding binding;

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = EditarPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot() );

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }
}
