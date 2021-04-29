package com.example.tncsito;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;
import java.io.ObjectOutputStream;

import static android.content.Context.MODE_PRIVATE;

public class dialogComeWithFragment extends DialogFragment {

    private String m;
    private int tipo;

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    //constructor
    public dialogComeWithFragment(int tipo) {
        this.tipo = tipo;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.dialog_trae, null);
        final EditText texto = (EditText)v.findViewById(R.id.texto);

        builder.setView(v)
                // Add action buttons
                .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String mensaje = texto.getText().toString();
                        setM(mensaje);
                        listener.onDialogPositiveClickCW(getM(),getTipo());
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialogComeWithFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }



    //Para enviar las respuesas al activity
    public interface dialogListener {
        public void onDialogPositiveClickCW(String mensaje, int tipo);
        //public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    dialogListener listener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (dialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }



}
