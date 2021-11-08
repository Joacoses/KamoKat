package com.example.dabebel.registro;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.dabebel.registro.databinding.EditarPerfilBinding;
import com.example.dabebel.registro.databinding.FragmentPestana1Binding;
import com.example.dabebel.registro.databinding.PerfilBinding;


public class Pestana1Fragment extends Fragment {

    private FragmentPestana1Binding binding;
    Button btnMapa;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_pestana1, container, false);
        View view = inflater.inflate(R.layout.fragment_pestana1, container, false);
        //binding = FragmentPestana1Binding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot() );

        /*btnMapa = view.findViewById(R.id.btn_1);
        btnMapa.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent( getContext(), Mapa.class);
                        startActivity(i);
                    }
                }
        );
*/
        /*binding.btn1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( getContext(), Mapa.class);
                startActivity(i);
            }
        });*/

        return view;
    }


}