package com.example.dabebel.registro;

import android.app.Notification;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.dabebel.registro.databinding.ActivityMainBinding;
import com.example.dabebel.registro.databinding.InvitarBinding;

public class Invitar extends AppCompatActivity {

    InvitarBinding binding;
    String text = "https://play.google.com/store/search?q=kamokat&hl=es&gl=US";

    public double puntosSumados = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = InvitarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot() );


       /* Double puntosMonedero;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                puntosMonedero= null;
            } else {
                puntosMonedero= extras.getDouble("PojoMonedero");
                puntosSumados = puntosMonedero;

            }
        } else {
            puntosMonedero= (Double) savedInstanceState.getSerializable("PojoMonedero");
        }*/



        Button invitar = (Button)findViewById(R.id.btnCopiar);

        invitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ClipboardManager myClipboard = myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip;

                myClip = ClipData.newPlainText("link", "https://play.google.com/store/search?q=kamokat&hl=es&gl=US");
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(Invitar.this, "Se ha copiado el link en el portapapeles", Toast.LENGTH_SHORT).show();

                    puntosSumados = puntosSumados+1;
                    binding.txtContadorPuntos.setText(Double.toString(puntosSumados));
                Intent intent2 = new Intent(Invitar.this, Monedero.class);
                intent2.putExtra("PuntosSumados",puntosSumados);
                startActivity(intent2);
                finish();

            }
        });
        Button correo = (Button)findViewById(R.id.btnCorreo);

        correo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               mandarCorreo(v);

            }
        });




    }
    public void mandarCorreo(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/search?q=kamokat&hl=es&gl=US");
        startActivity(intent);
        finish();


    }
}
