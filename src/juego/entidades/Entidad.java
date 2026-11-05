package juego.entidades;

import contratos.Actualizable;
import contratos.Dibujable;

public abstract class Entidad implements Actualizable, Dibujable {

    private double x, y;
    private double vx, vy;
    private double ancho, alto;

    protected Entidad(double x, double y, double ancho, double alto) {
        setAncho(ancho);
        setAlto(alto);
        setX(x);
        setY(y);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getVx() { return vx; }
    public double getVy() { return vy; }
    public double getAncho() { return ancho; }
    public double getAlto() { return alto; }

    public void setX(double x) {
        if (!Double.isFinite(x)) {
            throw new IllegalArgumentException("Posición X inválida");
        }
        this.x = x;
    }

    public void setY(double y) {
        if (!Double.isFinite(y)) {
            throw new IllegalArgumentException("Posición Y inválida");
        }
        this.y = y;
    }

    public void setVelocidad(double vx, double vy) {
        if (!Double.isFinite(vx) || !Double.isFinite(vy)) {
            throw new IllegalArgumentException("Velocidad inválida");
        }
        this.vx = vx;
        this.vy = vy;
    }

    public void setAncho(double ancho) {
        if (!(ancho > 0)) {
            throw new IllegalArgumentException("El ancho debe ser positivo");
        }
        this.ancho = ancho;
    }

    public void setAlto(double alto) {
        if (!(alto > 0)) {
            throw new IllegalArgumentException("El alto debe ser positivo");
        }
        this.alto = alto;
    }

    // Avanza la posición según la velocidad
    protected void mover(double dt) {
        x += vx * dt;
        y += vy * dt;
    }

    // Choque entre rectángulos
    protected boolean solapaCon(Colisionable o) {
        return x < o.getX() + o.getAncho()
            && x + ancho > o.getX()
            && y < o.getY() + o.getAlto()
            && y + alto > o.getY();
    }
}
