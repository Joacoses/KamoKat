package com.example.dabebel.registro;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.dabebel.registro.databinding.ActivityMainBinding;
import com.firebase.ui.auth.AuthUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private Map<String, String> datosUsuario = new HashMap<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ActivityMainBinding binding;

    //----------------------------------------------------------------------------------------------
    //Tabs

    private EditText entrada;
    private TextView salida;

    // Nombres de las pestañas
    private String[] nombres = new String[]{"1","2","3"};
    private int[] imageResId = {
            R.drawable.iconousuario,
            R.drawable.tabinfo,
            R.drawable.tarjetabanco,
    };
    //----------------------------------------------------------------------------------------------
    private Button btnCerrarSesion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot() );
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();




        //Floating Button---------------------------------------------------------------------------
        FloatingActionButton boton = findViewById(R.id.btnfcentral);
        FloatingActionButton botonMapa = findViewById(R.id.btnfmapa);
        FloatingActionButton botonTabs = findViewById(R.id.btnftabs);
        FloatingActionButton botonAcercade = findViewById(R.id.btnfacercade);
        FloatingActionButton botonInvitar = findViewById(R.id.btnfinvitar);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(botonMapa.getVisibility() == View.VISIBLE){
                    botonMapa.setVisibility(View.GONE);
                    botonMapa.setClickable(false);

                    botonTabs.setVisibility(View.GONE);
                    botonTabs.setClickable(false);

                    botonAcercade.setVisibility(View.GONE);
                    botonAcercade.setClickable(false);

                    botonInvitar.setVisibility(View.GONE);
                    botonInvitar.setClickable(false);
                }
                else {
                    botonMapa.setVisibility(View.VISIBLE);
                    botonMapa.setClickable(true);

                    botonTabs.setVisibility(View.VISIBLE);
                    botonTabs.setClickable(false);

                    botonAcercade.setVisibility(View.VISIBLE);
                    botonAcercade.setClickable(true);

                    botonInvitar.setVisibility(View.VISIBLE);
                    botonInvitar.setClickable(true);
                }

            }
        });
        //-----------------------------------------------------------------------------------------


        //------------------------------------------------------------------------------------------
        //Tabs
        //Pestañas

        ViewPager2 viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new MiPagerAdapter(this));
        TabLayout tabs = findViewById(R.id.tabs);
        new TabLayoutMediator(tabs, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position){
                        tab.setIcon(imageResId[position]);
                        //si queremos ver nombres o numeros:
                        //tab.setText(nombres[position]);
                    }
                }
        ).attach();



        //entrada = findViewById(R.id.entrada);
        //salida = findViewById(R.id.salida);
        //------------------------------------------------------------------------------------------



    }//onCreate

    //----------------------------------------------------------------------------------------------
    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    //----------------------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------------
    //Tabs
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.perfil) {
            lanzarPerfil(null);
            return true;
        }
        if (id == R.id.acercaDe) {
            lanzarAcercaDe(null);
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    public class MiPagerAdapter extends FragmentStateAdapter {
        public MiPagerAdapter(FragmentActivity activity){
            super(activity);
        }
        @Override
        public int getItemCount() {
            return 3;
        }
        @Override @NonNull
        public Fragment createFragment(int position) {
            switch (position) {
                case 0: return new Pestana1Fragment();
                case 1: return new Pestana2Fragment();
                case 2: return new Pestana3Fragment();
            }
            return null;
        }
    }


    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    //MENU Y ACERCA DE
    public void lanzarAcercaDe(View view){
        Intent i = new Intent(this, AcercaDeActivity.class);
        startActivity(i);
    }
    public void lanzarPerfil(View view){
        Intent i = new Intent(this, Perfil.class);
        startActivity(i);
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

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

    public void abrirMonedero(View view) {
        startActivity(new Intent(this,Monedero.class));
    }


    /*
    public void abrirInvitar(View view) {
        startActivity(new Intent(this,Invitar.class));
    }*/





}
