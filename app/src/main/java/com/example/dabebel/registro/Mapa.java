package com.example.dabebel.registro;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class Mapa extends FragmentActivity implements OnMapReadyCallback {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa);
        // Obtenemos el mapa de forma asíncrona (notificará cuando esté listo)
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
    }
    @Override public void onMapReady(GoogleMap googleMap) {
        GoogleMap mapa = googleMap;
        LatLng UPV = new LatLng(39.481106, -0.340987); //Nos ubicamos en la UPV
        mapa.addMarker(new MarkerOptions().position(UPV).title("Marker UPV"));
        mapa.moveCamera(CameraUpdateFactory.newLatLng(UPV));
    }


}
