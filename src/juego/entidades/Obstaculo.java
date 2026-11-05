package juego.entidades;

import java.awt.Color;
import java.awt.Graphics2D;

public class Obstaculo extends Entidad implements Colisionable {

    public Obstaculo(double x, double y, double ancho, double alto) {
        super(x, y, ancho, alto);
    }

    @Override
    public void actualizar(double dt) {
        // Está quieto: no hace nada
    }

    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(new Color(110, 115, 130));
        g.fillRect((int) getX(), (int) getY(), (int) getAncho(), (int) getAlto());
    }

    @Override
    public boolean choca(Colisionable otro) {
        return solapaCon(otro);
    }
}
