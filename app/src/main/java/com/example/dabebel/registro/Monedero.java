package com.example.dabebel.registro;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dabebel.registro.databinding.InvitarBinding;
import com.example.dabebel.registro.databinding.MonederoBinding;

public class Monedero extends AppCompatActivity {

    MonederoBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = MonederoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot() );

        Button puntos = (Button)findViewById(R.id.btnPuntos);

        puntos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               abrirInvitar(v);
            }
        });

    }//onCreate

    public void abrirInvitar(View view) {
        startActivity(new Intent(this,Invitar.class));
    }

}
