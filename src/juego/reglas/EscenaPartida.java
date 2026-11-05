package juego.reglas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import contratos.Dibujable;
import motor.Escena;

public class EscenaPartida extends Escena {

    private final Mundo mundo;
    private final Dibujable hud;
    private final AlTerminar alTerminar;
    private boolean cerrada;

    public EscenaPartida(Mundo mundo, Dibujable hud, AlTerminar alTerminar) {
        if (mundo == null || hud == null || alTerminar == null) {
            throw new IllegalArgumentException("La partida necesita mundo, HUD y destino al terminar");
        }
        this.mundo = mundo;
        this.hud = hud;
        this.alTerminar = alTerminar;
    }

    @Override
    public void alEntrar() {
        mundo.reiniciar();
        cerrada = false;
    }

    @Override
    public void actualizar(double dt) {
        if (getEntrada().huboClick()) {
            mundo.lanzar(getEntrada().getMouseX(), getEntrada().getMouseY());
        }
        mundo.actualizar(dt);

        if (getEntrada().fuePresionada(KeyEvent.VK_ESCAPE)) {
            getContexto().salir();
        }
        if (mundo.terminada() && !cerrada) {
            cerrada = true;
            getContexto().irA(alTerminar.crearEscenaFin(mundo.getPuntaje(), mundo.getAciertos()));
        }
    }

    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(new Color(20, 24, 36));
        g.fillRect(0, 0, getContexto().getAncho(), getContexto().getAlto());

        mundo.dibujar(g);

        // Guía de puntería: trayectoria de la próxima pelota
        mundo.dibujarTrayectoria(g, getEntrada().getMouseX(), getEntrada().getMouseY());
        g.setColor(new Color(255, 230, 90));
        g.fillOval((int) mundo.getOrigenX() - 6, (int) mundo.getOrigenY() - 6, 12, 12);

        hud.dibujar(g);
    }
}
