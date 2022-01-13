package com.example.dabebel.registro;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import static android.content.Context.NOTIFICATION_SERVICE;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class Pestana3Fragment extends Fragment {


    public Button botonTarjeta;

    public Button botonNoti;

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, String> viaje = new HashMap<>();
    String[] fotosEstaciones = {"https://firebasestorage.googleapis.com/v0/b/kamokat-1b330.appspot.com/o/paranimf.PNG?alt=media&token=7c9f9925-073d-4445-acc9-80aaabb01199"
    ,"https://firebasestorage.googleapis.com/v0/b/kamokat-1b330.appspot.com/o/burger.jpg?alt=media&token=183d355c-0b75-444f-8228-18a7432ea594",
    "https://firebasestorage.googleapis.com/v0/b/kamokat-1b330.appspot.com/o/upvimagen.jpg?alt=media&token=f0a4b019-dc0b-40a5-8b91-d499f50e08fc"};

    private NotificationManager notificationManager;
    static final String CANAL_ID = "mi_canal";
    static final int NOTIFICACION_ID = 1;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pestana3, container, false);

        mAuth = FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
        botonTarjeta = (Button) view.findViewById(R.id.botonViajes);

        //botonNoti = (Button) view.findViewById(R.id.btnNoti);




        Random rand = new Random();
        int coste = rand.nextInt((6-1)) + 1;
        Log.d("Coste",Integer.toString(coste) );
        int duracion = rand.nextInt((91-31)) + 31;
        Log.d("Duracion",Integer.toString(duracion) );
        int estacion = rand.nextInt((3-1)) + 1;
        Log.d("Estacion", Integer.toString(estacion));
        Date fecha = Calendar. getInstance(). getTime();
        Log.d("Fecha", fecha.toString());

        viaje.put("Coste", coste + "€" );
        if (duracion > 59)
        {
            viaje.put("Duracion", "1:" + (duracion - 60) + "h");

        }
        else
        {
            viaje.put("Duracion", duracion + "mins");

        }
        viaje.put("Estacion",Integer.toString(estacion) );
        viaje.put("Fecha", fecha.toString());
        viaje.put("Foto", fotosEstaciones[estacion - 1]);

        Log.d("UID", currentUser.getUid());
        crearViajes(currentUser.getUid());

        botonTarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //noti();
                Toast.makeText(getContext(),"Viaje creado",Toast.LENGTH_SHORT).show();

                crearViajes(currentUser.getUid());
            }
        });


/*
        botonNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                noti();


            }
        });

*/


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pestana3, container, false);
    }//onCreate

    public void noti(){



        Toast.makeText(getContext(),"funciona",Toast.LENGTH_LONG);

        NotificationCompat.Builder notificacion =

                new NotificationCompat.Builder(getContext(), CANAL_ID)

                        .setSmallIcon(R.drawable.iconopatineteredondo)
                        .setContentTitle("Kamokat informa:")
                        .setContentText("Te recordamos que tienes alquilado un patín");
        notificacion.setDefaults(Notification.DEFAULT_VIBRATE);
        notificacion.setPriority(Notification.PRIORITY_MAX);
        notificacion.setLights(Color.GREEN, 3000, 1000);
        notificacion.setUsesChronometer(true);
        notificacion.setOngoing(true);


        notificationManager = (NotificationManager)
                getActivity().getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(
                    CANAL_ID, "Mis Notificaciones",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Descripcion del canal");
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(NOTIFICACION_ID, notificacion.build());


    }

    public void crearViajes(String uID)
    {


        db.collection("Usuarios").document(currentUser.getUid()).collection("Viajes").add(viaje);
        Log.d("Viaje creado", viaje.toString());
    }

}//