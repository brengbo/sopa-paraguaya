package juego.reglas;

public class Acierto {

    private final int puntos;
    private final double densidad;
    private final double elasticidad;

    public Acierto(int puntos, double densidad, double elasticidad) {
        if (puntos <= 0) {
            throw new IllegalArgumentException("Los puntos deben ser positivos");
        }
        this.puntos = puntos;
        this.densidad = densidad;
        this.elasticidad = elasticidad;
    }

    public int getPuntos() { return puntos; }
    public double getDensidad() { return densidad; }
    public double getElasticidad() { return elasticidad; }
}
