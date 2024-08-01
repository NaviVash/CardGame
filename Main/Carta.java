package Main;

import javax.swing.*;

public class Carta {
    private String nombre;
    private int vida;
    private int coste;
    private int dano;
    private String imagen;

    public Carta(String nombre, int vida, int coste, int dano, String imagen) {
        this.nombre = nombre;
        this.vida = vida;
        this.coste = coste;
        this.dano = dano;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getCoste() {
        return coste;
    }

    public int getDano() {
        return dano;
    }

    public String getImagen() {
        return imagen;
    }
}
