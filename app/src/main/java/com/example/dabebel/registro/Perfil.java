package com.example.dabebel.registro;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
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
    private ImageView fotoUsuario;
    private TextView nombre;
    private TextView mail;
    private TextView fecha;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        nombre = findViewById(R.id.txtNombreUsuario);
        mail = findViewById(R.id.txtMailUsuario);
        fecha = findViewById(R.id.txtFechaUsuario);
        fotoUsuario = findViewById(R.id.fotoUsuario);

        cogerDatosUsuario(currentUser);

    }

    private void cargarDatosUsuario(Map<String, String> datosUsuario)
    {
        nombre.setText(datosUsuario.get("Nombre"));
        mail.setText(datosUsuario.get("Mail"));

        Date d=new Date(Long.parseLong(datosUsuario.get("Fecha")));
        fecha.setText(d.toString());


        try
        {
            Glide.with(this).load(datosUsuario.get("Foto")).into(fotoUsuario);
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
