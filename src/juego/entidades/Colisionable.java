package juego.entidades;

public interface Colisionable {
    double getX();
    double getY();
    double getAncho();
    double getAlto();

    boolean choca(Colisionable otro);
}
