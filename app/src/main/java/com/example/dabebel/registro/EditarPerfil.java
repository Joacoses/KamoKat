package com.example.dabebel.registro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dabebel.registro.databinding.ActivityMainBinding;
import com.example.dabebel.registro.databinding.EditarPerfilBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static android.content.ContentValues.TAG;

public class EditarPerfil extends AppCompatActivity {

    private EditarPerfilBinding binding;

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DatabaseReference mDB = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = EditarPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot() );

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        binding.btnGuardar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comprobarDatos())
                {
                    updateDatos();
                    Intent i = new Intent(EditarPerfil.this,Perfil.class);
                    startActivity(i);
                }
            }
        });

    }

    private boolean comprobarDatos()
    {
        if (!"".equals(binding.txtNombreEditar.getText().toString()))
        {
            if (!"".equals(binding.txtMailEditar.getText().toString()))
            {
                if (!"".equals(binding.txtContrasenyaEditar.getText().toString()))
                {
                    Toast.makeText(EditarPerfil.this, "Los campos estan llenos",
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
        }
        Toast.makeText(EditarPerfil.this, "Rellene los campos para editar el perfil",
                Toast.LENGTH_SHORT).show();
        return false;

    }

    private void updateDatos()
    {
        cambiarNombre(binding.txtNombreEditar.getText().toString());
        cambiarMailMail(binding.txtMailEditar.getText().toString());
        cambiarContrasenya(binding.txtContrasenyaEditar.getText().toString());
    }
    private void cambiarContrasenya(String contraNueva)
    {
        currentUser.updatePassword(contraNueva)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated.");
                        }
                    }
                });
    }

    private void cambiarMailMail(String nuevoMail)
    {
        currentUser.updateEmail(nuevoMail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address updated.");
                        }
                    }
                });

    }

    private void cambiarNombre(String nuevoNombre)
    {
        final String[] docID = {""};
        db.collection("Usuarios").whereEqualTo("Mail", currentUser.getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                               docID[0] = document.getId();
                            }
                        }
                    }
                });
        db.collection("Usuarios").document(docID[0]).update("Nombre", nuevoNombre);
    }
}
