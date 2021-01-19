package sample;

import java.util.ArrayList;

public class Variables {
    private String nombre;
    private String tipo;
    private String valor;

    public Variables(String nombre, String tipo, String valor) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
    }

    public Variables(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public Variables() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }


}
