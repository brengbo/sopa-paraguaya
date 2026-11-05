package juego.entidades;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Pelota extends Entidad implements Colisionable {

    private static final double GRAVEDAD = 600;
    private static final double RAPIDEZ_MAXIMA = 1400;

    private double densidad;
    private double elasticidad;

    public Pelota(double x, double y, double radio, double densidad, double elasticidad) {
        super(x, y, radio * 2, radio * 2);
        setDensidad(densidad);
        setElasticidad(elasticidad);
    }

    public void setDensidad(double densidad) {
        if (!(densidad > 0)) {
            throw new IllegalArgumentException("La densidad debe ser positiva");
        }
        this.densidad = densidad;
    }

    public void setElasticidad(double elasticidad) {
        if (!(elasticidad >= 0 && elasticidad <= 1)) {
            throw new IllegalArgumentException("La elasticidad va de 0 a 1");
        }
        this.elasticidad = elasticidad;
    }

    public double getDensidad() { return densidad; }
    public double getElasticidad() { return elasticidad; }

    // Más densa = más masa (con raíz cuadrada para que las pesadas no queden imposibles)
    public double getMasa() {
        double radio = getAncho() / 2;
        return Math.sqrt(densidad) * radio * radio / 100;
    }

    // Con la misma fuerza, una pelota pesada sale más lento
    public void lanzar(double fuerza, double anguloRadianes) {
        double rapidez = Math.min(fuerza / getMasa(), RAPIDEZ_MAXIMA);
        setVelocidad(rapidez * Math.cos(anguloRadianes),
                    -rapidez * Math.sin(anguloRadianes));
    }

    // Sobrecarga: lanzar con un vector (dx, dy) hacia donde apunta el jugador
    public void lanzar(double fuerza, double dx, double dy) {
        lanzar(fuerza, Math.atan2(-dy, dx));
    }

    public void rebotarVertical() {
        setVelocidad(getVx(), -getVy() * elasticidad);
    }

    public void rebotarHorizontal() {
        setVelocidad(-getVx() * elasticidad, getVy());
    }

    @Override
    public void actualizar(double dt) {
        setVelocidad(getVx(), getVy() + GRAVEDAD * dt);
        mover(dt);
    }

    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(colorSegunDensidad());
        g.fillOval((int) getX(), (int) getY(), (int) getAncho(), (int) getAlto());
        g.setColor(new Color(0, 0, 0, 120));
        g.setStroke(new BasicStroke(2f));
        g.drawOval((int) getX(), (int) getY(), (int) getAncho(), (int) getAlto());
    }

    private Color colorSegunDensidad() {
        if (densidad < 1.2) {
            return new Color(230, 100, 40);   // goma
        } else if (densidad < 1.8) {
            return new Color(160, 110, 60);   // madera
        } else if (densidad < 2.6) {
            return new Color(130, 130, 130);  // piedra
        }
        return new Color(150, 170, 200);      // acero
    }

    @Override
    public boolean choca(Colisionable otro) {
        return solapaCon(otro);
    }
}
