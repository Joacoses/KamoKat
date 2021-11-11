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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import com.example.dabebel.registro.databinding.PerfilBinding;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Perfil extends AppCompatActivity {
    ImageView fotousuario;
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

        binding.btnCerrarSesion.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion(null);
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

        Glide.with(this).load(datosUsuario.get("Foto")).into(binding.fotoUsuario);
        try
        {
            Glide.with(this).load(datosUsuario.get("Foto")).placeholder(R.drawable.iconopatineteredondo).into(binding.fotoUsuario);
        }
        catch (NullPointerException e)
        {
            binding.fotoUsuario.setImageResource(R.drawable.iconopatineteredondo);
            /*Toast.makeText(Perfil.this, "Fallo al descargar la información",
                    Toast.LENGTH_SHORT).show();*/
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
            /*Toast.makeText(Perfil.this, "Fallo al descargar la información",
                    Toast.LENGTH_SHORT).show();*/
        }

        cargarDatosUsuario(datosUsuario);
    }

    //cerrarSesion
    public void cerrarSesion(View view) {
        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override public void onComplete(@NonNull Task<Void> task) {
                        Intent i = new Intent(Perfil.this,Login.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        Perfil.this.finish();
                    }
                });
    }
}
