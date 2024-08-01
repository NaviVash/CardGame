package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

public class Juego {
    private Jugador jugador1;
    private Jugador jugador2;
    private boolean turnoJugador1;
    private JFrame frame;
    private JPanel panelTableroJugador1;
    private JPanel panelTableroJugador2;
    private JLabel labelJugador1;
    private JLabel labelJugador2;
    private JPanel panelManoJugador1;
    private JPanel panelManoJugador2;
    private JPanel panelAcciones;
    private JTextArea textoAcciones;
    private Carta cartaSeleccionada;
    private JButton botonPasarTurno;
    private boolean cartaJugada;

    public void iniciarPartida() {
        // Inicializar jugadores con mazos
        String imagePath = "C://Users//Ivan//Desktop//librojuego//original//";
        String imagePath2= "C://Users//Ivan//Desktop//librojuego//IA//webui_forge_cu121_torch21//webui//output//txt2img-images//2024-07-24//";
        
        Mazo mazo1 = new Mazo(generarCartas(imagePath2));
        Mazo mazo2 = new Mazo(generarCartas(imagePath));

        jugador1 = new Jugador("Jugador 1", 0, mazo1);
        jugador2 = new Jugador("Jugador 2", 0, mazo2);

        // Robar cartas iniciales
        for (int i = 0; i < 3; i++) {
            jugador1.robarCarta();
            jugador2.robarCarta();
        }
        jugador2.robarCarta(); // Carta extra para el segundo jugador

        // Decidir quién empieza
        turnoJugador1 = new Random().nextBoolean();

        // Inicializar la interfaz gráfica
        frame = new JFrame("Juego de Cartas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        panelTableroJugador1 = new JPanel();
        panelTableroJugador1.setLayout(new GridLayout(1, 5));
        frame.add(panelTableroJugador1, BorderLayout.CENTER);

        panelTableroJugador2 = new JPanel();
        panelTableroJugador2.setLayout(new GridLayout(1, 5));
        frame.add(panelTableroJugador2, BorderLayout.NORTH);

        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new GridLayout(1, 2));
        frame.add(panelInfo, BorderLayout.NORTH);

        labelJugador1 = new JLabel("Jugador 1 - Vida: " + jugador1.getVida());
        labelJugador2 = new JLabel("Jugador 2 - Vida: " + jugador2.getVida());
        panelInfo.add(labelJugador1);
        panelInfo.add(labelJugador2);

        panelManoJugador1 = new JPanel();
        panelManoJugador1.setLayout(new FlowLayout());
        frame.add(panelManoJugador1, BorderLayout.SOUTH);

        panelManoJugador2 = new JPanel();
        panelManoJugador2.setLayout(new FlowLayout());
        frame.add(panelManoJugador2, BorderLayout.EAST);
        panelManoJugador2.setVisible(false); // Ocultar mano del jugador 2

        panelAcciones = new JPanel();
        panelAcciones.setLayout(new FlowLayout());
        frame.add(panelAcciones, BorderLayout.WEST);

        textoAcciones = new JTextArea(5, 20);
        textoAcciones.setEditable(false);
        JScrollPane scrollAcciones = new JScrollPane(textoAcciones);
        frame.add(scrollAcciones, BorderLayout.EAST);

        botonPasarTurno = new JButton("Pasar Turno");
        botonPasarTurno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pasarTurno();
            }
        });
        panelAcciones.add(botonPasarTurno);

        actualizarTablero();
        actualizarMano(jugador1);

        frame.setVisible(true);

        imprimirAccion("Empieza el turno de " + (turnoJugador1 ? "Jugador 1" : "Jugador 2"));
        if (turnoJugador1) {
            ejecutarTurno(jugador1, jugador2, labelJugador1);
        } else {
            ejecutarTurno(jugador2, jugador1, labelJugador2);
        }
    }

    private void pasarTurno() {
        turnoJugador1 = !turnoJugador1;
        imprimirAccion("Turno de " + (turnoJugador1 ? "Jugador 1" : "Jugador 2"));
        if (turnoJugador1) {
            ejecutarTurno(jugador1, jugador2, labelJugador1);
        } else {
            simularTurnoJugador2();
        }
    }

    private void ejecutarTurno(Jugador jugador, Jugador oponente, JLabel labelJugador) {
        jugador.robarCarta();
        actualizarTablero();
        actualizarMano(jugador);
        labelJugador.setText(jugador.getNombre() + " - Vida: " + jugador.getVida());

        cartaJugada = false;

        if (turnoJugador1) {
            // Permitir al jugador tomar decisiones solo si es el turno del jugador 1
            for (Carta carta : jugador.getMano()) {
                JButton botonCarta = crearBotonCarta(carta);
                botonCarta.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!cartaJugada) {
                            jugador.jugarCarta(carta);
                            imprimirAccion("Jugador 1 juega la carta " + carta.getNombre());
                            actualizarTablero();
                            actualizarMano(jugador);
                            actualizarAcciones(jugador, oponente);
                            cartaJugada = true;
                        }
                    }
                });
                panelManoJugador1.add(botonCarta);
            }
        }
        frame.revalidate();
        frame.repaint();
    }

    private void actualizarTablero() {
        panelTableroJugador1.removeAll();
        for (Carta carta : jugador1.getTablero()) {
            JLabel labelCarta = crearLabelCarta(carta);
            labelCarta.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    cartaSeleccionada = carta;
                    imprimirAccion("Carta seleccionada: " + carta.getNombre());
                    actualizarAcciones(jugador1, jugador2);
                }
            });
            panelTableroJugador1.add(labelCarta);
        }

        panelTableroJugador2.removeAll();
        for (Carta carta : jugador2.getTablero()) {
            JLabel labelCarta = crearLabelCarta(carta);
            labelCarta.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (cartaSeleccionada != null && turnoJugador1) {
                        jugador1.atacarCarta(cartaSeleccionada, carta);
                        imprimirAccion("Jugador 1 ataca con " + cartaSeleccionada.getNombre() + " a " + carta.getNombre());
                        actualizarTablero();
                        actualizarAcciones(jugador1, jugador2);
                        cartaSeleccionada = null;
                    }
                }
            });
            panelTableroJugador2.add(labelCarta);
        }

        labelJugador1.setText("Jugador 1 - Vida: " + jugador1.getVida());
        labelJugador2.setText("Jugador 2 - Vida: " + jugador2.getVida());
        panelTableroJugador1.revalidate();
        panelTableroJugador2.revalidate();
        panelTableroJugador1.repaint();
        panelTableroJugador2.repaint();
    }

    private void actualizarMano(Jugador jugador) {
        if (jugador == jugador1) {
            panelManoJugador1.removeAll();
            for (Carta carta : jugador.getMano()) {
                JButton botonCarta = crearBotonCarta(carta);
                panelManoJugador1.add(botonCarta);
            }
        } else {
            panelManoJugador2.removeAll();
            for (Carta carta : jugador.getMano()) {
                // No mostrar las cartas del jugador 2
                JButton botonCarta = new JButton("Carta Oculta");
                panelManoJugador2.add(botonCarta);
            }
        }
        panelManoJugador1.revalidate();
        panelManoJugador2.revalidate();
        panelManoJugador1.repaint();
        panelManoJugador2.repaint();
    }

    private void actualizarAcciones(Jugador jugador, Jugador oponente) {
        panelAcciones.removeAll();
        if (cartaSeleccionada != null) {
            JButton atacarJugador = new JButton("Atacar Jugador");
            atacarJugador.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    jugador.atacarJugador(cartaSeleccionada, oponente);
                    imprimirAccion(jugador.getNombre() + " ataca a " + oponente.getNombre() + " con " + cartaSeleccionada.getNombre());
                    cartaSeleccionada = null;
                    actualizarTablero();
                    actualizarAcciones(jugador, oponente);
                    pasarTurno();
                }
            });
            panelAcciones.add(atacarJugador);

            JButton atacarCarta = new JButton("Atacar Carta");
            atacarCarta.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (Carta carta : oponente.getTablero()) {
                        if (cartaSeleccionada != null) {
                            jugador.atacarCarta(cartaSeleccionada, carta);
                            imprimirAccion(jugador.getNombre() + " ataca a la carta " + carta.getNombre() + " con " + cartaSeleccionada.getNombre());
                            cartaSeleccionada = null;
                            actualizarTablero();
                            actualizarAcciones(jugador, oponente);
                            pasarTurno();
                            break;
                        }
                    }
                }
            });
            panelAcciones.add(atacarCarta);
        }
        panelAcciones.revalidate();
        panelAcciones.repaint();
    }

    private JButton crearBotonCarta(Carta carta) {
        ImageIcon icon = new ImageIcon(carta.getImagen());
        Image img = icon.getImage().getScaledInstance(50, 70, Image.SCALE_SMOOTH);
        JButton botonCarta = new JButton(new ImageIcon(img));
        botonCarta.setToolTipText(carta.getNombre() + " (Vida: " + carta.getVida() + ", Coste: " + carta.getCoste() + ", Daño: " + carta.getDano() + ")");
        return botonCarta;
    }

    private JLabel crearLabelCarta(Carta carta) {
        ImageIcon icon = new ImageIcon(carta.getImagen());
        Image img = icon.getImage().getScaledInstance(50, 70, Image.SCALE_SMOOTH);
        JLabel labelCarta = new JLabel(new ImageIcon(img));
        labelCarta.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        labelCarta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                labelCarta.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labelCarta.setBorder(BorderFactory.createLineBorder(Color.BLUE));
            }
        });
        return labelCarta;
    }

    private ArrayList<Carta> generarCartas(String imagePath) {
        ArrayList<Carta> cartas = new ArrayList<>();
        // Generar 20 cartas con estadísticas y imágenes aleatorias
        for (int i = 0; i < 20; i++) {
        	String Path= imagePath+ "descarga (" + (i % 5 + 1) + ").png";
        	System.out.println(Path);
            cartas.add(new Carta("Carta " + (i + 1), new Random().nextInt(10), new Random().nextInt(5), new Random().nextInt(3), Path));
        }
        return cartas;
    }

    private void imprimirAccion(String accion) {
        textoAcciones.append(accion + "\n");
    }

    private void simularTurnoJugador2() {
        // Simular desplegar una carta
        if (!jugador2.getMano().isEmpty() && !cartaJugada) {
            Carta carta = jugador2.getMano().get(0);
            jugador2.jugarCarta(carta);
            imprimirAccion("Jugador 2 juega la carta " + carta.getNombre());
            actualizarTablero();
            cartaJugada = true;
        }

        // Simular atacar con una carta
        if (!jugador2.getTablero().isEmpty()) {
            Carta atacante = jugador2.getTablero().get(0);
            if (!jugador1.getTablero().isEmpty()) {
                // Atacar una carta en el tablero del jugador 1
                Carta objetivo = jugador1.getTablero().get(0);
                jugador2.atacarCarta(atacante, objetivo);
                imprimirAccion("Jugador 2 ataca con " + atacante.getNombre() + " a " + objetivo.getNombre());
            } else {
                // Atacar directamente al jugador 1
                jugador2.atacarJugador(atacante, jugador1);
                imprimirAccion("Jugador 2 ataca directamente a Jugador 1 con " + atacante.getNombre());
            }
            actualizarTablero();
        }

        pasarTurno();
    }
}
