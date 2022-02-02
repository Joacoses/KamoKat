package com.example.dabebel.registro;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dabebel.registro.databinding.InvitarBinding;
import com.example.dabebel.registro.databinding.MonederoBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;


public class Monedero extends AppCompatActivity {

    MonederoBinding binding;

    private FirebaseFirestore db;

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        db= FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        double saldo = 100;
        double punto = 0;
        double puntoDinero = punto * 0.02;
        double total = saldo + puntoDinero;


        HashMap<String, Double> moneder =new HashMap<String, Double>();

        moneder.put("Saldo", saldo);
        moneder.put("Puntos", punto);
        moneder.put("PuntosDinero", puntoDinero);
        moneder.put("Total", total);


        FirebaseFirestore.getInstance()
                .collection("Usuarios").document(currentUser.getUid())
                .collection("Monedero").document()
                .set(moneder);

  
       FirebaseFirestore.getInstance()
                .collection("Usuarios").document(currentUser.getUid())
                .collection("Monedero").document("Saldo").get();
        FirebaseFirestore.getInstance()
                .collection("Usuarios").document(currentUser.getUid())
                .collection("Monedero").document("Puntos").get();



        binding = MonederoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot() );

        Button btnpuntos = (Button)findViewById(R.id.btnPuntos);

        btnpuntos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               abrirInvitar(v);
            }
        });




      POJO_MONEDERO monedero = new POJO_MONEDERO(saldo,punto,puntoDinero,total);




        /*Intent intent = new Intent(Monedero.this, Invitar.class);
        intent.putExtra("PojoMonedero",monedero.getPuntos());
*/

        Double puntosTotales;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
               puntosTotales= null;

            } else {

                puntosTotales= extras.getDouble("PuntosSumados");
                monedero.setPuntos(puntosTotales);
                double dineroTotal = puntosTotales * 0.02 + monedero.getTotal();
                monedero.setPuntosDinero(puntosTotales * 0.02);
                monedero.setTotal(dineroTotal);


            }
        } else {
            puntosTotales= (Double) savedInstanceState.getSerializable("PuntosSumados");
        }





        binding.txtPuntosDinero.setText(Double.toString(monedero.getPuntosDinero()));
        binding.txtTotal.setText(Double.toString(monedero.getTotal()));
        binding.txtSaldo.setText(Double.toString(monedero.getSaldo()));
        binding.txtPuntos.setText(Double.toString(monedero.getPuntos()));

    }//onCreate

    public void abrirInvitar(View view) {
        startActivity(new Intent(this,Invitar.class));
        finish();
    }




}
