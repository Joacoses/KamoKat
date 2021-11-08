package com.example.dabebel.registro;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Mapa2 extends Fragment implements OnMapReadyCallback /*, GoogleMap*/ {
    private GoogleMap mMap;
    private GoogleMap mapa;
    private final LatLng Estacion1 = new LatLng(38.995480, -0.165029);
    SupportMapFragment mapFragment;
    public Mapa2(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.mapa2, container, false);


/*
        Button boton1 = (Button) view.findViewById(R.id.centrar);
        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapa.animateCamera(CameraUpdateFactory.newLatLng(Estacion1));
            }
        });
*/



        mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.mapa);
        if (mapFragment == null){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.mapa, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);


        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        mapa = googleMap;
        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapa.getUiSettings().setZoomControlsEnabled(false);
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(Estacion1, 19));
        mapa.addMarker(new MarkerOptions().position(Estacion1).title("Estación C/ Paranimf").snippet("46722 Gandia, Valencia"));
        /*mapa.addMarker(new MarkerOptions()
                .position(Pisc)
                .title("Piscimar")
                .snippet("Piscimar")
                .icon(BitmapDescriptorFactory
                        .fromResource(android.R.drawable.ic_menu_compass))
                .anchor(0.5f, 0.5f));
*/

        //descomentar si se quiere que al hacer click en el mapa se añada una marca
        //mapa.setOnMapClickListener(this);



        if (ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mapa.setMyLocationEnabled(true);
            mapa.getUiSettings().setCompassEnabled(true);
        }
    }














//Añade un marca al pulsar en el mapa
    /*
    @Override public void onMapClick(LatLng puntoPulsado) {
        mapa.addMarker(new MarkerOptions().position(puntoPulsado)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
    }*/







    /*@Override
    public void onDestroyView() {
        super.onDestroyView();
        SupportMapFragment f= (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.mapa);
        if (f != null)
            getFragmentManager().beginTransaction().remove(f).commit();
    }*/

}



