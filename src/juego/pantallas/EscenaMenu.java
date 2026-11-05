package juego.pantallas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import motor.Escena;
import motor.Temporizador;
import ui.Texto;

public class EscenaMenu extends Escena {

    private final Escena siguiente;
    private final Temporizador parpadeo = new Temporizador(0.6);
    private boolean visible = true;

    public EscenaMenu(Escena siguiente) {
        if (siguiente == null) {
            throw new IllegalArgumentException("El menú necesita una escena siguiente");
        }
        this.siguiente = siguiente;
    }

    @Override
    public void alEntrar() {
        visible = true;
        parpadeo.reiniciar();
    }

    @Override
    public void actualizar(double dt) {
        if (parpadeo.avanzar(dt)) {
            visible = !visible;
        }
        if (getEntrada().fuePresionada(KeyEvent.VK_ENTER)) {
            getContexto().irA(siguiente);
        }
        if (getEntrada().fuePresionada(KeyEvent.VK_ESCAPE)) {
            getContexto().salir();
        }
    }

    @Override
    public void dibujar(Graphics2D g) {
        int ancho = getContexto().getAncho();
        int alto = getContexto().getAlto();
        g.setColor(new Color(20, 24, 36));
        g.fillRect(0, 0, ancho, alto);

        Texto.centrado(g, "LABORATORIO DE REBOTE", ancho / 2.0, alto / 2.0 - 90, 48, Color.WHITE);
        Texto.centrado(g, "Lanzá pelotas de distintos materiales y embocalas en el aro",
                ancho / 2.0, alto / 2.0 - 40, 18, Color.LIGHT_GRAY);
        Texto.centrado(g, "Apuntá con el mouse (los puntitos muestran el camino) y hacé click: más lejos = más fuerza",
                ancho / 2.0, alto / 2.0 - 8, 16, Color.GRAY);
        Texto.centrado(g, "Las pelotas pesadas salen más lento. Las poco elásticas se frenan en el piso.",
                ancho / 2.0, alto / 2.0 + 16, 16, Color.GRAY);
        Texto.centrado(g, "Tenés 3 vidas: perdés una cada vez que una pelota se queda en el piso.",
                ancho / 2.0, alto / 2.0 + 40, 16, Color.GRAY);
        if (visible) {
            Texto.centrado(g, "ENTER: jugar     ESC: salir", ancho / 2.0, alto / 2.0 + 110, 22,
                    new Color(255, 230, 90));
        }
    }
}
