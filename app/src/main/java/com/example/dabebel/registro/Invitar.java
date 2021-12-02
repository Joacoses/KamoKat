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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = InvitarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot() );
        Button invitar = (Button)findViewById(R.id.btnCopiar);

        invitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager myClipboard = myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip;

                myClip = ClipData.newPlainText("link", "https://play.google.com/store/search?q=kamokat&hl=es&gl=US");
                myClipboard.setPrimaryClip(myClip);
                mandarCorreo(v);
                Toast.makeText(Invitar.this, "Se ha copiado el link en el portapapeles", Toast.LENGTH_SHORT).show();

            }
        });


    }
    public void mandarCorreo(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/search?q=kamokat&hl=es&gl=US");
        startActivity(intent);

    }
}
