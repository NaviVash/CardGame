package Main;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
            	System.out.println("Iniciando Juego");
                Juego juego = new Juego();
                juego.iniciarPartida();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
