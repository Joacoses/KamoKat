package com.example.dabebel.registro;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Mapa extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private GoogleMap mapa;
    private final LatLng posInicial = new LatLng(38.996952, -0.1636356);


    private NotificationManager notificationManager;
    static final String CANAL_ID = "mi_canal";
    static final int NOTIFICACION_ID = 1;

    final String location = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final int LOCALIZACION = 0;
    final String camara = Manifest.permission.CAMERA;
    public static final int CAMARA = 2;
    final String nfc = Manifest.permission.NFC;
    public static final int NFC = 3;
    final String noti = Manifest.permission.ACCESS_NOTIFICATION_POLICY;
    public static final int NOTIFICACIONES = 4;
    final String escribirStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final int ESCRIBIR = 5;
    final String leerStorage = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final int LEER = 6;
    final String manageStorage = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final int MANAGE = 7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa);
        // Obtenemos el mapa de forma asíncrona (notificará cuando esté listo)
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);

        //Floating Button---------------------------------------------------------------------------
        FloatingActionButton boton = findViewById(R.id.btnfcentral);
        FloatingActionButton botonMapa = findViewById(R.id.btnfmapa);
        FloatingActionButton botonTabs = findViewById(R.id.btnftabs);
        FloatingActionButton botonAcercade = findViewById(R.id.btnfacercade);
        FloatingActionButton botonInvitar = findViewById(R.id.btnfinvitar);

        //crearPuntos();

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (botonMapa.getVisibility() == View.VISIBLE) {
                    botonMapa.setVisibility(View.GONE);
                    botonMapa.setClickable(false);

                    botonTabs.setVisibility(View.GONE);
                    botonTabs.setClickable(false);

                    botonAcercade.setVisibility(View.GONE);
                    botonAcercade.setClickable(false);

                    botonInvitar.setVisibility(View.GONE);
                    botonInvitar.setClickable(false);
                } else {
                    botonMapa.setVisibility(View.VISIBLE);
                    botonMapa.setClickable(false);

                    botonTabs.setVisibility(View.VISIBLE);
                    botonTabs.setClickable(true);

                    botonAcercade.setVisibility(View.VISIBLE);
                    botonAcercade.setClickable(true);

                    botonInvitar.setVisibility(View.VISIBLE);
                    botonInvitar.setClickable(true);
                }

            }
        });

        solicitarPermiso(location, "Sin el permiso"+
                        " no funciona el mapa",
                LOCALIZACION, this);
        solicitarPermiso(noti, "Sin el permiso"+
                        " no funciona el mapa",
                NOTIFICACIONES, this);
        solicitarPermiso(camara, "Sin el permiso"+
                        " no se pueden hacer fotos",
                CAMARA, this);
        solicitarPermiso(nfc, "Sin el permiso"+
                        " no se puede activar el servo",
                NFC, this);
        solicitarPermiso(escribirStorage, "Sin el permiso"+
                        " no se pueden guardar datos en el storage",
                ESCRIBIR, this);
        solicitarPermiso(leerStorage, "Sin el permiso"+
                        " no se pueden leer datos de el storage",
                LEER, this);
        solicitarPermiso(manageStorage, "Sin el permiso"+
                        " no se pueden leer datos de el storage",
                MANAGE, this);

        //------------------------------------------------------------------------------------------

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(posInicial, 12));
        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapa.getUiSettings().setZoomControlsEnabled(false);
        descargarCoord();


    }


    //Floating Button-------------------------------------------------------------
    public void abrirMapa(View view) {
        startActivity(new Intent(this,Mapa.class));
    }

    public void abrirTabs(View view) {
        startActivity(new Intent(this,MainActivity.class));
    }

    public void abrirAcercaDe(View view) {
        startActivity(new Intent(this,AcercaDeActivity.class));
    }

    public void descargarCoord()
    {
        db.collection("Estaciones").get() .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        cargarPuntos(document);
                    }
                }
            }
        });
    }

    public void cargarPuntos(QueryDocumentSnapshot document)
    {
        Log.d("Coord",document.toString() );
        GeoPoint coords = (GeoPoint) document.get("Coordenadas");
        LatLng coordenadas = new LatLng(coords.getLatitude(), coords.getLongitude()); //Nos ubicamos en la UPV
        mapa.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title(document.get("Nombe").toString())
                .snippet("Cantidad de patinetes: " + document.get("Patinetes disponibles").toString() + " / " + document.get("Total patinetes").toString()));
        mapa.setOnMapClickListener(this);
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mapa.setMyLocationEnabled(true);
            mapa.getUiSettings().setCompassEnabled(true);

        }
    }
    public void moveCamera(View view) {
        mapa.moveCamera(CameraUpdateFactory.newLatLng(posInicial));
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

    }


/*
    public void crearPuntos()
    {
            GeoPoint coords = new GeoPoint(38.9959748,-0.1657578);

            Map<String, Object> punto = new HashMap<>();
            punto.put("Coordenadas", coords );
            punto.put("Nombe", "Estacion 1");
            punto.put("Patinetes disponibles", 4);
            punto.put("Total patinetes", 5);
            db.collection("Estaciones").document().set(punto);
        Log.d("Coord", "Punto creado" + punto.toString() );
    }

    public void abrirInvitar(View view) {
        startActivity(new Intent(this,Invitar.class));
    }*/



    public static void solicitarPermiso(final String permiso, String
            justificacion, final int requestCode, final Activity actividad) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(actividad,
                permiso)){
            new AlertDialog.Builder(actividad)
                    .setTitle("Solicitud de permiso")
                    .setMessage(justificacion)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ActivityCompat.requestPermissions(actividad,
                                    new String[]{permiso}, requestCode);
                        }}).show();
        } else {
            ActivityCompat.requestPermissions(actividad,
                    new String[]{permiso}, requestCode);
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, String[]
            permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if (requestCode == ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
            if (grantResults.length== 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Tienes el permiso", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Sin el permiso, no puedo realizar la " +
                        "acción", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
