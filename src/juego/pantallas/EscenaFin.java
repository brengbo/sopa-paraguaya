package juego.pantallas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import motor.Escena;
import ui.Texto;

public class EscenaFin extends Escena {

    private final int puntaje;
    private final int aciertos;
    private final Escena volver;

    public EscenaFin(int puntaje, int aciertos, Escena volver) {
        if (puntaje < 0 || aciertos < 0) {
            throw new IllegalArgumentException("El puntaje y los aciertos no pueden ser negativos");
        }
        if (volver == null) {
            throw new IllegalArgumentException("Falta la escena a la que volver");
        }
        this.puntaje = puntaje;
        this.aciertos = aciertos;
        this.volver = volver;
    }

    @Override
    public void actualizar(double dt) {
        if (getEntrada().fuePresionada(KeyEvent.VK_ENTER)) {
            getContexto().irA(volver);
        }
        if (getEntrada().fuePresionada(KeyEvent.VK_ESCAPE)) {
            getContexto().salir();
        }
    }

    @Override
    public void dibujar(Graphics2D g) {
        int ancho = getContexto().getAncho();
        int alto = getContexto().getAlto();
        g.setColor(new Color(36, 24, 40));
        g.fillRect(0, 0, ancho, alto);

        Texto.centrado(g, "FIN DE LA PARTIDA", ancho / 2.0, alto / 2.0 - 80, 44, Color.WHITE);
        Texto.centrado(g, "Puntaje: " + puntaje, ancho / 2.0, alto / 2.0 - 20, 34, new Color(255, 230, 90));
        Texto.centrado(g, "Aciertos: " + aciertos, ancho / 2.0, alto / 2.0 + 20, 22, Color.LIGHT_GRAY);
        Texto.centrado(g, comentario(), ancho / 2.0, alto / 2.0 + 60, 18, Color.GRAY);
        Texto.centrado(g, "ENTER: volver al menú     ESC: salir", ancho / 2.0, alto / 2.0 + 120, 18, Color.WHITE);
    }

    private String comentario() {
        if (aciertos == 0) {
            return "Ningún acierto: probá con más fuerza para las pelotas pesadas";
        } else if (aciertos < 4) {
            return "Buen comienzo, el aro se pone más rápido con cada acierto";
        }
        return "¡Gran puntería!";
    }
}
