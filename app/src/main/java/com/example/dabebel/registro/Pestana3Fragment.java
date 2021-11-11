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


public class Pestana3Fragment extends Fragment {


    public Button botonTarjeta;

    public Button botonNoti;


    private NotificationManager notificationManager;
    static final String CANAL_ID = "mi_canal";
    static final int NOTIFICACION_ID = 1;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pestana3, container, false);


        botonTarjeta = (Button) view.findViewById(R.id.btn_3);

        botonNoti = (Button) view.findViewById(R.id.btnNoti);


        noti();


        botonTarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });



        botonNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                noti();


            }
        });




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

}//