package juego.entidades;

import java.awt.Color;
import java.awt.Graphics2D;

public class Aro extends Entidad implements Colisionable {

    private double limiteIzq;
    private double limiteDer;

    public Aro(double x, double y, double ancho, double limiteIzq, double limiteDer, double rapidez) {
        super(x, y, ancho, 16);
        if (!(limiteDer > limiteIzq)) {
            throw new IllegalArgumentException("Límites inválidos");
        }
        this.limiteIzq = limiteIzq;
        this.limiteDer = limiteDer;
        setVelocidad(rapidez, 0);
    }

    @Override
    public void actualizar(double dt) {
        mover(dt);
        if (getX() < limiteIzq) {
            setX(limiteIzq);
            setVelocidad(-getVx(), 0);
        } else if (getX() + getAncho() > limiteDer) {
            setX(limiteDer - getAncho());
            setVelocidad(-getVx(), 0);
        }
    }

    @Override
    public void dibujar(Graphics2D g) {
        int x = (int) getX();
        int y = (int) getY();
        int w = (int) getAncho();
        g.setColor(new Color(150, 30, 30));
        g.fillRect(x, y - 24, 5, 24);          // poste izquierdo
        g.fillRect(x + w - 5, y - 24, 5, 24);  // poste derecho
        g.setColor(new Color(230, 60, 60));
        g.fillRect(x, y, w, (int) getAlto());  // la boca del aro
    }

    // Embocó: el centro de la pelota está dentro de la boca del aro
    @Override
    public boolean choca(Colisionable otro) {
        double cx = otro.getX() + otro.getAncho() / 2;
        double cy = otro.getY() + otro.getAlto() / 2;
        return cx > getX() && cx < getX() + getAncho()
            && cy > getY() && cy < getY() + getAlto();
    }
}
