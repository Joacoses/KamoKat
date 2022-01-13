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



public class Monedero extends AppCompatActivity {

    MonederoBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = MonederoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot() );

        Button btnpuntos = (Button)findViewById(R.id.btnPuntos);

        btnpuntos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               abrirInvitar(v);
            }
        });


      double saldo = 100;
      double punto = 0;
      double puntoDinero = punto * 0.02;
      double total = saldo + puntoDinero;

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
