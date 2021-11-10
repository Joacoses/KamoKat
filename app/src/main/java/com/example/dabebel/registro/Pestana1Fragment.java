package com.example.dabebel.registro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.example.dabebel.registro.databinding.EditarPerfilBinding;
import com.example.dabebel.registro.databinding.FragmentPestana1Binding;
import com.example.dabebel.registro.databinding.PerfilBinding;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Pestana1Fragment extends Fragment {

    ImageView fotousuario;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private Map<String, String> datosUsuario = new HashMap<>();
    //private PerfilBinding binding;

    Button btnEditarPerfil;
    Button btnCerrarSesion;
    TextView nombreUsuario;
    TextView mailUsusario;
    TextView fechaUsusario;
    ImageView imagenUsusario;
    /*@Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*binding = PerfilBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot() );
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

    }*/

    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.perfil, container, false);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        btnEditarPerfil = view.findViewById(R.id.btnEditar);
        btnEditarPerfil.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editarPerfil();
                    }
                }
        );

        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cerrarSesion(null);
                    }
                }
        );



        nombreUsuario = view.findViewById(R.id.txtNombreUsuario);
        mailUsusario = view.findViewById(R.id.txtMailUsuario);
        fechaUsusario = view.findViewById(R.id.txtFechaUsuario);
        imagenUsusario = view.findViewById(R.id.fotoUsuario);


        cogerDatosUsuario(currentUser);

    return view;
    }

    private void editarPerfil()
    {
        Intent i = new Intent( getContext(), EditarPerfil.class);
        startActivity(i);
    }
    private void cargarDatosUsuario(Map<String, String> datosUsuario)
    {

        nombreUsuario.setText(datosUsuario.get("Nombre"));
        mailUsusario.setText(datosUsuario.get("Mail"));

        Date d=new Date(Long.parseLong(datosUsuario.get("Fecha")));
        fechaUsusario.setText(d.toString());


        try
        {
            Glide.with(this).load(datosUsuario.get("Foto")).placeholder(R.drawable.iconopatineteredondo).into(fotousuario);
        }
        catch (NullPointerException e)
        {
            imagenUsusario.setImageResource(R.drawable.iconopatineteredondo);
            Toast.makeText(getContext(), "Fallo al descargar la información",
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
            Toast.makeText(getContext(), "Fallo al descargar la información",
                    Toast.LENGTH_SHORT).show();
        }

        cargarDatosUsuario(datosUsuario);
    }

    //cerrarSesion
    public void cerrarSesion(View view) {
        AuthUI.getInstance().signOut(getContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override public void onComplete(@NonNull Task<Void> task) {
                        Intent i = new Intent(getContext(),Login.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        getActivity().finish();
                    }
                });
    }






















/*
    private FragmentPestana1Binding binding;
    Button btnMapa;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_pestana1, container, false);
        View view = inflater.inflate(R.layout.fragment_pestana1, container, false);
        //binding = FragmentPestana1Binding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot() );

        /*btnMapa = view.findViewById(R.id.btn_1);
        btnMapa.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent( getContext(), Mapa.class);
                        startActivity(i);
                    }
                }
        );
*/
        /*binding.btn1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( getContext(), Mapa.class);
                startActivity(i);
            }
        });*/
/*
        return view;
    }
*/

}