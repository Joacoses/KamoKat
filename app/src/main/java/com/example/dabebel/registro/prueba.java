package com.example.dabebel.registro;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GRAY;
import static android.graphics.Color.RED;

import static com.example.comun.Mqtt.broker;
import static com.example.comun.Mqtt.clientId;
import static com.example.comun.Mqtt.qos;
import static com.example.comun.Mqtt.topicRoot;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;

import com.example.comun.Mqtt;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.HashMap;
import java.util.Map;


public class prueba extends Fragment implements org.eclipse.paho.client.mqttv3.MqttCallback{



    Button btnAbrirCerrar;

    int valor;


    //MQTT
    private static MqttClient client;



    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.prueba, container, false);




        //MQTT

        conectarMqtt();
        suscribirMqtt("arduino", this);

        //--------------------------------



        btnAbrirCerrar = view.findViewById(R.id.btnAbrirCerrar);
        btnAbrirCerrar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if(btnAbrirCerrar.getText().equals("ABRIR")){
                            //ENVIAR MQTT CON VALOR 1
                            //--CAMBIA A:
                            publicarMqtt("arduino","1");
                            btnAbrirCerrar.setEnabled(false);
                            btnAbrirCerrar.setBackgroundResource(R.drawable.btn_gris);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Do something after 5s = 5000ms
                                    btnAbrirCerrar.setEnabled(true);
                                    btnAbrirCerrar.setBackgroundResource(R.drawable.btn_aceptar);
                                }
                            }, 20000);
                        }


                    }
                }
        );





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




        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //publicarMqtt("arduino","1");
        //Toast.makeText(getContext(), "Topic publicado",Toast.LENGTH_LONG).show();

    }
    //MQTT------------------------------------------------------------------------------------------

    public static void conectarMqtt() {
        try {
            Log.i(Mqtt.TAG, "Conectando al broker " + broker);
            client = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(60);
            connOpts.setWill(topicRoot+"WillTopic","App desconectada".getBytes(),
                    qos, false);
            client.connect(connOpts);
        } catch (MqttException e) {
            Log.e(Mqtt.TAG, "Error al conectar.", e);
        }
    }

    public static void publicarMqtt(String topic, String mensageStr) {
        try {
            MqttMessage message = new MqttMessage(mensageStr.getBytes());
            message.setQos(qos);
            message.setRetained(false);
            client.publish(topicRoot + topic, message);
            Log.i(Mqtt.TAG, "Publicando mensaje: " + topic+ "->"+mensageStr);
        } catch (MqttException e) {
            Log.e(Mqtt.TAG, "Error al publicar." + e);
        }
    }

    public static void suscribirMqtt(String topic, MqttCallback listener) {
        try {
            Log.i(Mqtt.TAG, "Suscrito a " + topicRoot + topic);
            //Log.i(Mqtt.TAG, "Mensaje recibido: Hola, somos el grupo2_2" );
            client.subscribe(topicRoot + topic, qos);
            client.setCallback(listener);
        } catch (MqttException e) {
            Log.e(Mqtt.TAG, "Error al suscribir.", e);
        }
    }


    public static void deconectarMqtt() {
        try {
            client.disconnect();
            Log.i(Mqtt.TAG, "Desconectado");
        } catch (MqttException e) {
            Log.e(Mqtt.TAG, "Error al desconectar.", e);
        }
    }
    @Override
    public void onDestroy() {
        deconectarMqtt();
        super.onDestroy();
    }

    @Override
    public void connectionLost(Throwable cause) {
        Log.d(Mqtt.TAG, "Conexión perdida");
    }
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d(Mqtt.TAG, "Entrega completa");
    }
    @Override
    public void messageArrived(String topic, MqttMessage message)
            throws Exception {
        String payload = new String(message.getPayload());
        Log.d(Mqtt.TAG, "Recibiendo: " + topic + "->" + payload);
    }
}
