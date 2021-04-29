package com.example.tncsito;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements dialogComeWithFragment.dialogListener, dialogYesNoFragment.dialogListenerYesNo {

    MaterialButton come, comeWith, yesNo, emergency,leave;

    //private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    List<Pedido> listPedidos = new ArrayList<>();

    private int tipo;
    private String mensaje;
    //getters setters
    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        come = findViewById(R.id.btnOnlyCome);
        comeWith = findViewById(R.id.btnComeWith);
        yesNo = findViewById(R.id.btnYesNo);
        emergency = findViewById(R.id.btnEmergency);
        leave = findViewById(R.id.btnLeaveOut);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("req");
        //mAuth = FirebaseAuth.getInstance();

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
                    databaseReference.push().setValue(new Pedido(tipo, " "," "));
                    Toast.makeText(getApplicationContext(),"Llamado de emergencia enviado 👌",Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"No se pudo enviar :c. Revise su conexión a internet.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        comeWith.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogComeWithFragment dComeWith = new dialogComeWithFragment(2);
                dComeWith.show(getSupportFragmentManager(),"Ven con");
                /*tipo = 2;
                databaseReference.push().setValue(new Pedido(tipo, " "," "));*/
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

    }

    //Añadir Pedido
    public void addPedido(Pedido p){
        listPedidos.add(p);
    }


    //Actuar de acuerdo al boton que presione
    @Override
    public void onDialogPositiveClickYesNo(String mensaje){
        setMensaje(mensaje);
        try {
            databaseReference.push().setValue(new Pedido(4, " ",getMensaje()));
            Toast.makeText(getApplicationContext(),"Llamado de emergencia enviado 👌",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"No se pudo enviar :c. Revise su conexión a internet.",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDialogPositiveClickCW(String mensaje, int tipo) {
        Log.i("Entro con","tipo = "+tipo+" msg = "+mensaje);
        setMensaje(mensaje);
        try {
            if (tipo == 2){
                databaseReference.push().setValue(new Pedido(tipo, " ",getMensaje()));
                Toast.makeText(getApplicationContext(),"Pedido enviado 👌",Toast.LENGTH_SHORT).show();
            }
            if(tipo == 3){
                databaseReference.push().setValue(new Pedido(tipo, " ",getMensaje()));
                Toast.makeText(getApplicationContext(),"Pregunta enviada 👌",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"No se pudo enviar :c. Revise su conexión a internet.",Toast.LENGTH_SHORT).show();
        }

    }
}