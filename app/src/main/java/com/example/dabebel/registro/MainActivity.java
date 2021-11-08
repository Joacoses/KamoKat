package com.example.dabebel.registro;

import android.content.Intent;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.dabebel.registro.databinding.ActivityMainBinding;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
            R.drawable.tabmapa,
            R.drawable.tabcampana,
            R.drawable.tabinfo,
            R.drawable.tabinfo
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
        cogerDatosUsuario(currentUser);
        comprobarUsuario(datosUsuario);


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
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.cerrarSesion) {
            cerrarPerfil(null);
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

        return super.onOptionsItemSelected(item);
    }

    public class MiPagerAdapter extends FragmentStateAdapter {
        public MiPagerAdapter(FragmentActivity activity){
            super(activity);
        }
        @Override
        public int getItemCount() {
            return 4;
        }
        @Override @NonNull
        public Fragment createFragment(int position) {
            switch (position) {
                case 0: return new Mapa2();
                case 1: return new Pestana1Fragment();
                case 2: return new Pestana2Fragment();
                case 3: return new Pestana3Fragment();
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
    public void cerrarPerfil(View view){
        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override public void onComplete(@NonNull Task<Void> task) {
                        Intent i = new Intent(MainActivity.this,Login.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        MainActivity.this.finish();
                    }
                });
    }
    //----------------------------------------------------------------------------------------------



    public void cerrarSesion(View view) {
        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override public void onComplete(@NonNull Task<Void> task) {
                        Intent i = new Intent(MainActivity.this,Login.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        MainActivity.this.finish();
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

    }
    private void comprobarUsuario(Map<String, String> datosASubir)
    {



        try {
            db.collection("Usuarios").whereEqualTo("Mail", datosASubir.get("Mail")).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.d("Query", task.getResult().getDocuments().toString());
                                Log.d("Datos Usuario", datosUsuario.toString());

                                if (task.getResult().isEmpty())
                                {
                                    subirDatosUsuario(datosUsuario);
                                }
                            }
                        }
                    });
        }
        catch (NullPointerException e)
        {

        }

    }

    public void cogerDatosUsuario(FirebaseUser currentUser)
    {

        try
        {
            datosUsuario.put("Nombre", currentUser.getDisplayName());
            datosUsuario.put("Mail", currentUser.getEmail());
            datosUsuario.put("Foto", currentUser.getPhotoUrl().toString());
        }
        catch (NullPointerException e)
        {
            Toast.makeText(MainActivity.this, "Fallo al descargar la información",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void subirDatosUsuario(Map<String, String> datosASubir )
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Usuarios").add(datosASubir);
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
    /*
    public void abrirInvitar(View view) {
        startActivity(new Intent(this,Invitar.class));
    }*/

}
