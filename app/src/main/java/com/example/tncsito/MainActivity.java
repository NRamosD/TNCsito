package com.example.tncsito;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements dialogComeWithFragment.dialogListener, dialogYesNoFragment.dialogListenerYesNo, dialogWho.dialogListener {

    MaterialButton come, comeWith, yesNo, emergency,leave;
    FloatingActionButton connectToDb;

    //variables
    private int tipo;
    private String mensaje;
    private String nombre="";
    private String PREFS_DATA = "datos";

    //getters setters
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public int getTipo() { return tipo; }
    public void setTipo(int tipo) { this.tipo = tipo; }
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    //bd
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    List<Pedido> listPedidos = new ArrayList<>();
    SharedPreferences.OnSharedPreferenceChangeListener listenerPreferences;
    SharedPreferences preferencias;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        come = findViewById(R.id.btnOnlyCome);
        comeWith = findViewById(R.id.btnComeWith);
        yesNo = findViewById(R.id.btnYesNo);
        emergency = findViewById(R.id.btnEmergency);
        leave = findViewById(R.id.btnLeaveOut);
        connectToDb = findViewById(R.id.fabConect);

        try {
            database = FirebaseDatabase.getInstance();
            preferencias = getSharedPreferences(PREFS_DATA, Context.MODE_PRIVATE);
            databaseReference = database.getReference("bdtncsito/rooms/"+preferencias.getString("nameRoom","")+"/orders");
        }catch (Exception exc){
            Log.i("Problema al conectar 😂",exc.toString());
            Toast.makeText(getApplicationContext(),"Error al conectarse 😕",Toast.LENGTH_SHORT).show();
        }

        //BOTONES
        //Conectar a la sala
            connectToDb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Dialog para poner la data
                    dialogWho dWho = new dialogWho();
                    dWho.show(getSupportFragmentManager(),"quien es");
                }
            });
        //Salir
            leave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishAffinity();
                }
            });

        //Pedidos
            come.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        tipo = 1;
                        databaseReference.push().setValue(new Pedido(tipo, getNombre()," "));
                        Toast.makeText(getApplicationContext(),"Llamado de emergencia enviado 👌",Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"No se pudo enviar :c.",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            comeWith.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);

                    Toast.makeText(getApplicationContext(),"sala: "+preferencias.getString("nameRoom","")+" pass: "+preferencias.getString("passRoom",""),Toast.LENGTH_SHORT).show();*/
                    dialogComeWithFragment dComeWith = new dialogComeWithFragment(2);
                    dComeWith.show(getSupportFragmentManager(),"Ven con");

                }
            });
            yesNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogComeWithFragment dComeWith = new dialogComeWithFragment(3);
                    dComeWith.show(getSupportFragmentManager(),"SiNo");
                }
            });
            emergency.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogYesNoFragment dYesNo = new dialogYesNoFragment(4);
                    dYesNo.show(getSupportFragmentManager(),"SiNo");
                }
            });
        //Listeners de la app
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Pedido p = dataSnapshot.getValue(Pedido.class);
                addPedido(p);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listenerPreferences = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                try {
                    SharedPreferences preferencias = getSharedPreferences(PREFS_DATA, Context.MODE_PRIVATE);
                    databaseReference = database.getReference("bdtncsito/rooms/"+preferencias.getString("nameRoom","")+"/orders");
                }catch (Exception exc){
                    Log.i("Error al buscar la sala",exc.toString());
                    Toast.makeText(getApplicationContext(),"No se encuentra la sala 😕",Toast.LENGTH_SHORT).show();
                }

            }
        };
        preferencias.registerOnSharedPreferenceChangeListener(listenerPreferences);
    }

    //Añadir Pedido
    public void addPedido(Pedido p){
        listPedidos.add(p);
    }

    //Acciones de los dialogs
    @Override
    public void onDialogPositiveClickYesNo(String mensaje){
        setMensaje(mensaje);
        try {
            databaseReference.push().setValue(new Pedido(4, getNombre(),getMensaje()));
            Toast.makeText(getApplicationContext(),"Llamado de emergencia enviado 👌",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"No se pudo enviar :c.",Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onDialogPositiveClickCW(String mensaje, int tipo) {
        Log.i("Entro con","tipo = "+tipo+" msg = "+mensaje);
        setMensaje(mensaje);
        try {
            if (tipo == 2){
                databaseReference.push().setValue(new Pedido(tipo, getNombre(),getMensaje()));
                Toast.makeText(getApplicationContext(),"Pedido enviado 👌",Toast.LENGTH_SHORT).show();
            }
            if(tipo == 3){
                databaseReference.push().setValue(new Pedido(tipo, getNombre(),getMensaje()));
                Toast.makeText(getApplicationContext(),"Pregunta enviada 👌",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"No se pudo enviar :c.",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDialogPositiveClickWho(String nombre, String nameRoom, String passRoom) {
        if (nombre.equals("")){
            setNombre("Anónimo");
            saveData(nombre, nameRoom, passRoom);
        }else{
            setNombre(nombre);
            saveData(nombre, nameRoom, passRoom);
        }
    }


    //Persistencia
    public void saveData(String nombre, String nameRoom, String passRoom) {
        preferencias = getSharedPreferences(PREFS_DATA, Context.MODE_PRIVATE);
        Editor editor=preferencias.edit();
        editor.putString("nameRoom", nameRoom);
        editor.putString("passRoom", passRoom);
        editor.putString("user", nombre);
        editor.commit();
    }

}