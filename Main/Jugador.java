package Main;

import java.util.ArrayList;

public class Jugador {
    private String nombre;
    private int experiencia;
    private Mazo mazo;
    private int vida;
    private ArrayList<Carta> mano;
    private ArrayList<Carta> tablero;

    public Jugador(String nombre, int experiencia, Mazo mazo) {
        this.nombre = nombre;
        this.experiencia = experiencia;
        this.mazo = mazo;
        this.vida = 30; // Vida inicial
        this.mano = new ArrayList<>();
        this.tablero = new ArrayList<>();
    }

    public void robarCarta() {
        if (!mazo.estaVacio()) {
            Carta carta = mazo.robar();
            mano.add(carta);
        }
    }

    public void jugarCarta(Carta carta) {
        mano.remove(carta);
        tablero.add(carta);
    }

    public String getNombre() {
        return nombre;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public Mazo getMazo() {
        return mazo;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public ArrayList<Carta> getMano() {
        return mano;
    }

    public ArrayList<Carta> getTablero() {
        return tablero;
    }

    public void atacarCarta(Carta atacante, Carta objetivo) {
        objetivo.setVida(objetivo.getVida() - atacante.getDano());
        atacante.setVida(atacante.getVida() - objetivo.getDano());
        if (objetivo.getVida() <= 0) {
            tablero.remove(objetivo);
        }
    }

    public void atacarJugador(Carta atacante, Jugador oponente) {
        oponente.setVida(oponente.getVida() - atacante.getDano());
    }
}