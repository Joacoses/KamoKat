package com.example.dabebel.registro;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

public class Mapa extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private GoogleMap mapa;
    private final LatLng UPV = new LatLng(38.996952, -0.1636356);


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

        //------------------------------------------------------------------------------------------

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(UPV, 10));
        descargarCoord();


    }


    public boolean onMarkerClick(final Marker marker) {
        LatLng puntoMarcador = marker.getPosition();
        Toast.makeText(Mapa.this,marker.toString(),
                Toast.LENGTH_SHORT).show();
        db.collection("Estaciones").whereEqualTo("Coordenadas", puntoMarcador).get() .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Toast.makeText(Mapa.this, document.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return true;
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
        Toast.makeText(Mapa.this, document.get("Coordenadas").toString(),
                Toast.LENGTH_SHORT).show();
        Log.d("Coord",document.get("Coordenadas").toString() );
        GeoPoint coords = (GeoPoint) document.get("Coordenadas");
        LatLng coordenadas = new LatLng(coords.getLatitude(), coords.getLongitude()); //Nos ubicamos en la UPV
        mapa.addMarker(new MarkerOptions().position(coordenadas).title("Marker"));
        mapa.setOnMarkerClickListener(this);
        //mapa.moveCamera(CameraUpdateFactory.newLatLng(UPV));
    }
    public void moveCamera(View view) {
        mapa.moveCamera(CameraUpdateFactory.newLatLng(UPV));
    }

    /*public void crearPuntos()
    {
            GeoPoint coords = new GeoPoint(39.0033117,-0.1616843);

            Map<String, Object> punto = new HashMap<>();
            punto.put("Coordenadas", coords );
            punto.put("Nombe", "Estacion 2");
            punto.put("Patinetes disponibles", 4);
            punto.put("Total patinetes", 5);
            db.collection("Estaciones").document().set(punto);
        Log.d("Coord", "Punto creado" + punto.toString() );
    }

    public void abrirInvitar(View view) {
        startActivity(new Intent(this,Invitar.class));
    }*/
}
