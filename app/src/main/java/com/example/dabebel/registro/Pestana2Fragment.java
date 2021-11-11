package com.example.dabebel.registro;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class Pestana2Fragment extends Fragment {
    //RecyclerView
    private Adaptador adaptador;
    Uri urlFoto;
    Query query;


    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pestana2, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // 4.	RECICLERVIEW
        RecyclerView recyclerExam = view.findViewById(R.id.recycler1);

        //recyclerViewCargar();

        Query query = FirebaseFirestore.getInstance()
                .collection("Usuarios").document("eaLUYvJ2SBwe0YNkLGuN").collection("Viajes").orderBy("Fecha");
        FirestoreRecyclerOptions<POJO> opciones = new FirestoreRecyclerOptions
                .Builder<POJO>().setQuery(query, POJO.class).build();
        adaptador = new Adaptador(getContext(), opciones);
        recyclerExam.setAdapter(adaptador);
        recyclerExam.setLayoutManager(new LinearLayoutManager(getContext()));

        adaptador.onDataChanged();



        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        String coste = usuario.getDisplayName();
        String correo = usuario.getEmail();
        String telefono = usuario.getPhoneNumber();
        urlFoto= usuario.getPhotoUrl();
        String uid = usuario.getUid();
        Map<String, String> datosUsuario = new HashMap<>();
        datosUsuario.put("Nombre",coste);
        datosUsuario.put("Correo", correo);
        datosUsuario.put("Tel", telefono);

        try {
            datosUsuario.put("URL", urlFoto.toString());
        }catch (NullPointerException e){
            Toast.makeText(getContext(),"No tiene foto",Toast.LENGTH_SHORT).show();
        }



        datosUsuario.put("UID", uid);
        //textview.findViewById(R.id.nombredeltextviewdellayout);
        //textview.setText(nombre);
        Log.d("Datos usuario", datosUsuario.toString());

        return view;
    }


    //Adaptador
    @Override
    public void onStart() {
        super.onStart();
        adaptador.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adaptador.stopListening();
    }


    public void recyclerViewCargar(){
        final String[] docID = {""};
        FirebaseFirestore.getInstance().collection("Usuarios").whereEqualTo("Mail", currentUser.getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                docID[0] = document.getId();

                                Log.d("id documento",docID[0]);
                            }
                        }
                    }
                });
        query= FirebaseFirestore.getInstance().collection("Usuarios")
                .document(docID[0]).collection("Viajes");

    }



}