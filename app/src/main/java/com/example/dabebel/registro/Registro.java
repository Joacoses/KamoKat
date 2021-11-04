package com.example.dabebel.registro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dabebel.registro.databinding.ActivityMainBinding;
import com.example.dabebel.registro.databinding.RegistroBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Registro extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, String> datosASubir = new HashMap<>();
    private RegistroBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot() );
        mAuth = FirebaseAuth.getInstance();


        binding.btContinuar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        datosASubir.put("Nombre", recogerNombre());
                        datosASubir.put("Apellido", recogerApellido());
                        datosASubir.put("Mail", recogerMail());
                        datosASubir.put("Contrasenya", recogerContra());

                        comprobarUsuario(datosASubir);
                    }
                }
        );
    }

    public void registrar()
    {

        try {
            mAuth.createUserWithEmailAndPassword(datosASubir.get("Mail"), datosASubir.get("Contrasenya")).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        subirDatosUsuario(datosASubir);
                        FirebaseUser user = mAuth.getCurrentUser();

                    } else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(Registro.this, "No se pudo recuperar la información", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

        catch (IllegalArgumentException e)
        {
            Toast.makeText(Registro.this, "Para continuar rellene todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void comprobarUsuario(Map<String, String> datosASubir)
    {



        try {
            db.collection("Usuarios").whereEqualTo("Mail", datosASubir.get("Mail")).whereEqualTo("Contrasenya",datosASubir.get("Contrasenya")).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.d("Query", task.getResult().getDocuments().toString());
                        if (task.getResult().isEmpty())
                        {
                            registrar();
                        }
                    } else {
                        Toast.makeText(Registro.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        catch (NullPointerException e)
        {

        }

    }

    private void subirDatosUsuario(Map<String, String> datosASubir )
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Usuarios").add(datosASubir);
    }

    public String recogerNombre()
    {
        String nombre = "";
        nombre = binding.txtNombre.getText().toString();

        return nombre;
    }

    public String recogerApellido()
    {
        String apellido = "";
        apellido = binding.txtApellidos.getText().toString();

        return apellido;
    }

    public String recogerMail()
    {
        String mail = "";
        mail = binding.txtMail.getText().toString();

        return mail;
    }

    public String recogerContra()
    {
        String contra = "";
        contra = binding.txtContraseña.getText().toString();

        return contra;
    }

}