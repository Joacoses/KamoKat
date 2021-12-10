package com.example.dabebel.registro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dabebel.registro.databinding.ActivityMainBinding;
import com.example.dabebel.registro.databinding.EditarPerfilBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.content.ContentValues.TAG;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditarPerfil extends AppCompatActivity {

    private EditarPerfilBinding binding;

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DatabaseReference mDB = FirebaseDatabase.getInstance().getReference();
    String docID = "";
    //--------------------------------------------------------------------------------------------//
    //Subir y descargar imagen
    //--------------------------------------------------------------------------------------------//
    private StorageReference refBBDD;
    String urlImg;
    private Map<String, String> datosUsuario = new HashMap<>();
    //--------------------------------------------------------------------------------------------//
    //FIN
    //--------------------------------------------------------------------------------------------//

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
        binding.btnEditarImagen.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_PICK);
                        i.setType("image/*");
                        startActivityForResult(i, 1234);
                    }
                }
        );

        //----------------------------------------------------------------------------------------//
        //Subir y descargar imagen
        //----------------------------------------------------------------------------------------//
        refBBDD = FirebaseStorage.getInstance().getReference();
        cogerDatosUsuario(currentUser);

        setImg(currentUser.getEmail());
        //----------------------------------------------------------------------------------------//
        //FIN
        //----------------------------------------------------------------------------------------//

    }


    //--------------------------------------------------------------------------------------------//
    //Subir y descargar imagen
    //--------------------------------------------------------------------------------------------//
    @Override
    public void onActivityResult(final int requestCode,
                                 final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1234) {
                subirImg(data.getData(), "Imagenes/" + datosUsuario.get("Mail").toString());
            }
        }
    }

    public void cogerDatosUsuario(FirebaseUser currentUser)
    {
        datosUsuario.put("Mail", currentUser.getEmail());
    }


    public void setImg(String nombreDelTanque)
    {
        File localFile = null;
        try {
            localFile = File.createTempFile("image", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        final String path = localFile.getAbsolutePath();
        Log.d("Img" , "Nombre de la imagen: " + currentUser.getEmail() );
        StorageReference ficheroRef = refBBDD.child("Imagenes/" + currentUser.getEmail());
        ficheroRef.getFile(localFile)
                .addOnSuccessListener(new
                                              OnSuccessListener<FileDownloadTask.TaskSnapshot>(){
                                                  @Override
                                                  public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot){
                                                      Log.d("Almacenamiento", "Fichero bajado");
                                                      binding.fotoUsuario.setImageBitmap(BitmapFactory.decodeFile(path));
                                                  }
                                              }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                //Carga esta imagen si no hay ninguna en firebase
                binding.fotoUsuario.setImageResource(R.drawable.iconopatineteredondo);


                Log.e("Almacenamiento", "ERROR: bajando fichero");
            }
        });
    }

    //----------------------------------------------------------------------------------------//
    //FIN
    //----------------------------------------------------------------------------------------//


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
        Intent i = new Intent(this,Mapa.class);
        startActivity(i);
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
                        Log.d("Doc id 2", docID);

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
                            db.collection("Usuarios").document(currentUser.getUid()).update("Mail",nuevoMail);
                        }
                    }
                });

    }

    private void cambiarNombre(String nuevoNombre)
    {

        db.collection("Usuarios").whereEqualTo("Mail", currentUser.getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                               docID = document.getId();
                               Log.d("Doc id", docID);
                                db.collection("Usuarios").document(currentUser.getUid()).update("Nombre",nuevoNombre);

                            }
                    }
                });

    }



    private void subirImg(Uri archivo, String ref)
    {
        StorageReference ficheroRef = refBBDD.child(ref);
        ficheroRef.putFile(archivo).addOnSuccessListener(
                new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();
                        if(downloadUri.isSuccessful())
                        {
                            urlImg = downloadUri.getResult().toString();
                        }
                    }
                }
        );
    }
}




