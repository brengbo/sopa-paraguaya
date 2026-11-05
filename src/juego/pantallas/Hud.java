package juego.pantallas;

import java.awt.Color;
import java.awt.Graphics2D;

import contratos.Dibujable;
import juego.reglas.Estadisticas;
import ui.Texto;

public class Hud implements Dibujable {

    private final Estadisticas estadisticas;
    private final int ancho;
    private final int alto;

    public Hud(Estadisticas estadisticas, int ancho, int alto) {
        if (estadisticas == null) {
            throw new IllegalArgumentException("Faltan las estadísticas");
        }
        if (ancho <= 0 || alto <= 0) {
            throw new IllegalArgumentException("Tamaño inválido");
        }
        this.estadisticas = estadisticas;
        this.ancho = ancho;
        this.alto = alto;
    }

    @Override
    public void dibujar(Graphics2D g) {
        Texto.izquierda(g, "Puntaje: " + estadisticas.getPuntaje(), 16, 28, 18, Color.WHITE);
        Texto.centrado(g, estadisticas.getMensaje(), ancho / 2.0, 28, 15, Color.LIGHT_GRAY);
        String derecha = "Vidas: " + estadisticas.getVidas()
                + "   Pelotas: " + estadisticas.getPelotasEnCargador();
        Texto.derecha(g, derecha, ancho - 16, 28, 18, Color.WHITE);
        Texto.izquierda(g, estadisticas.getSiguiente(), 16, alto - 14, 15, new Color(255, 230, 90));
    }
}
