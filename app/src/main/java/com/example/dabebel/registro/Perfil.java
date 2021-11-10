package com.example.dabebel.registro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.dabebel.registro.databinding.ActivityMainBinding;
import com.example.dabebel.registro.databinding.PerfilBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Perfil extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private Map<String, String> datosUsuario = new HashMap<>();
    private PerfilBinding binding;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot() );
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        binding.btnEditar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               editarPerfil();
            }
        });

        cogerDatosUsuario(currentUser);

    }



    private void editarPerfil()
    {
        Intent i = new Intent( this, EditarPerfil.class);
        startActivity(i);
    }
    private void cargarDatosUsuario(Map<String, String> datosUsuario)
    {
        binding.txtNombreUsuario.setText(datosUsuario.get("Nombre"));
        binding.txtMailUsuario.setText(datosUsuario.get("Mail"));

        Date d=new Date(Long.parseLong(datosUsuario.get("Fecha")));
        binding.txtFechaUsuario.setText(d.toString());


        try
        {
            Glide.with(this).load(datosUsuario.get("Foto")).into(binding.fotoUsuario);
        }
        catch (NullPointerException e)
        {
            Toast.makeText(Perfil.this, "Fallo al descargar la información",
                    Toast.LENGTH_SHORT).show();
        }
    }

    //Coger datos usuario
    public void cogerDatosUsuario(FirebaseUser currentUser)
    {

        datosUsuario.put("Nombre", currentUser.getDisplayName());
        datosUsuario.put("Mail", currentUser.getEmail());
        datosUsuario.put("Fecha", Long.toString( currentUser.getMetadata().getCreationTimestamp()));
        try
        {
            datosUsuario.put("Foto", currentUser.getPhotoUrl().toString());
        }
        catch (NullPointerException e)
        {
            Toast.makeText(Perfil.this, "Fallo al descargar la información",
                    Toast.LENGTH_SHORT).show();
        }

        cargarDatosUsuario(datosUsuario);
    }
}
