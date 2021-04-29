package com.example.tncsito;

public class Pedido {
    private int tipo;
    private String remitente;
    private String mensaje;

    public Pedido(){}

    public Pedido(int tipo, String remitente, String mensaje) {
        this.tipo = tipo;
        this.remitente = remitente;
        this.mensaje = mensaje;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
