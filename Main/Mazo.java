package Main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Mazo {
    private Stack<Carta> cartas;

    public Mazo(ArrayList<Carta> cartas) {
        this.cartas = new Stack<>();
        Collections.shuffle(cartas);
        this.cartas.addAll(cartas);
    }

    public Carta robar() {
        if (!cartas.isEmpty()) {
            return cartas.pop();
        }
        return null;
    }

    public boolean estaVacio() {
        return cartas.isEmpty();
    }
}
