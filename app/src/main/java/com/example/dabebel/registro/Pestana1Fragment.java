package com.example.dabebel.registro;

import android.content.Intent;
import android.graphics.BitmapFactory;
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

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Pestana1Fragment extends Fragment {

    ImageView fotousuario;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private Map<String, String> datosUsuario = new HashMap<>();
    //private PerfilBinding binding;

    private StorageReference refBBDD;
    String urlImg;


    Button btnEditarPerfil;
    Button btnCerrarSesion;
    TextView nombreUsuario;
    TextView mailUsusario;
    TextView fechaUsusario;
    ImageView imagenUsuario;
    //ImageButton botonEditarImagen;

    FirebaseFirestore db = FirebaseFirestore.getInstance();



    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.perfil, container, false);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db= FirebaseFirestore.getInstance();


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
        imagenUsuario = view.findViewById(R.id.fotoUsuario);

        //boton para subir imagen
/*
        botonEditarImagen = view.findViewById(R.id.btnEditarImagen);
        botonEditarImagen.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_PICK);
                        i.setType("image/*");
                        startActivityForResult(i, 1234);
                    }
                }
        );*/


        refBBDD = FirebaseStorage.getInstance().getReference();

        cogerDatosUsuario(currentUser);
        setImg(currentUser.getEmail());

    return view;
    }

    //Subir imagen
/*
    @Override
    public void onActivityResult(final int requestCode,
                                 final int resultCode, final Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1234) {
                subirImg(data.getData(), "Imagenes/" + mailUsusario.getText());
            }


        }


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
    }*/

    private void editarPerfil()
    {
        Intent i = new Intent( getContext(), EditarPerfil.class);
        startActivity(i);
    } private void cargarDatosUsuario(Map<String, String> datosUsuario)
    {

        nombreUsuario.setText(datosUsuario.get("Nombre"));
        mailUsusario.setText(datosUsuario.get("Mail"));

        Date d=new Date(Long.parseLong(datosUsuario.get("Fecha")));
        fechaUsusario.setText(d.toString());

        Glide.with(this).load(datosUsuario.get("Foto")).into(imagenUsuario);
        try
        {
            Glide.with(this).load(datosUsuario.get("Foto")).placeholder(R.drawable.iconopatineteredondo).into(imagenUsuario);
        }
        catch (NullPointerException e)
        {
            fotousuario.setImageResource(R.drawable.iconopatineteredondo);
            /*Toast.makeText(Perfil.this, "Fallo al descargar la información",
                    Toast.LENGTH_SHORT).show();*/
        }
    }

    //Coger datos usuario
    public void cogerDatosUsuario(FirebaseUser currentUser)
    {

        final String[] nombre = {""};
        final String[] mail = {""};
        db.collection("Usuarios").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {

                    nombre[0] = task.getResult().getString("Nombre");
                    datosUsuario.put("Nombre", nombre[0]);
                    mail[0] = task.getResult().getString("Mail");
                    datosUsuario.put("Mail", mail[0]);
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
                    Log.d("PERFIL", datosUsuario.toString());
                }

            }
        });



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
                                                      imagenUsuario.setImageBitmap(BitmapFactory.decodeFile(path));
                                                  }
                                              }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                //Carga esta imagen si no hay ninguna en firebase
                imagenUsuario.setImageResource(R.drawable.iconopatineteredondo);


                Log.e("Almacenamiento", "ERROR: bajando fichero");
            }
        });
    }

}