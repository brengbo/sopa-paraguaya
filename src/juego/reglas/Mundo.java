package juego.reglas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import estructuras.Cola;
import estructuras.Lista;
import estructuras.MiCola;
import estructuras.MiLista;
import estructuras.MiPila;
import estructuras.Pila;
import juego.entidades.Aro;
import juego.entidades.Entidad;
import juego.entidades.Obstaculo;
import juego.entidades.Pelota;

public class Mundo implements Estadisticas {

    private static final double RADIO = 12;
    private static final int PELOTAS_INICIALES = 10;
    private static final int VIDAS_INICIALES = 3;
    private static final int PUNTOS_POR_ACIERTO = 10;
    private static final double DISTANCIA_MAXIMA = 400;
    private static final double FACTOR_FUERZA = 8;

    private final double ancho;
    private final double alto;
    private final double origenX;
    private final double origenY;
    private final Random azar = new Random();

    // Todo lo que se actualiza y se dibuja (pelotas, aro, obstáculos)
    private final Lista<Entidad> entidades = new MiLista<>();
    // Listas auxiliares: permiten chocar sin usar instanceof
    private final Lista<Pelota> enVuelo = new MiLista<>();
    private final Lista<Obstaculo> obstaculos = new MiLista<>();
    // Cargador: primera en entrar, primera en salir
    private final Cola<Pelota> cargador = new MiCola<>();
    // Historial: el último acierto queda arriba
    private final Pila<Acierto> historial = new MiPila<>();

    private Aro aro;
    private int puntaje;
    private int vidas;

    public Mundo(double ancho, double alto) {
        if (!(ancho > 0) || !(alto > 0)) {
            throw new IllegalArgumentException("El mundo necesita un tamaño positivo");
        }
        this.ancho = ancho;
        this.alto = alto;
        this.origenX = 60;
        this.origenY = alto - 40;
        reiniciar();
    }

    // Deja el mundo listo para una partida nueva
    public final void reiniciar() {
        entidades.vaciar();
        enVuelo.vaciar();
        obstaculos.vaciar();
        cargador.vaciar();
        historial.vaciar();
        puntaje = 0;
        vidas = VIDAS_INICIALES;

        aro = new Aro(ancho / 2 - 50, alto * 0.3, 100, 200, ancho - 20, 90);
        entidades.agregar(aro);

        Obstaculo pilar = new Obstaculo(ancho * 0.45, alto * 0.55, 20, alto * 0.3);
        entidades.agregar(pilar);
        obstaculos.agregar(pilar);

        for (int i = 0; i < PELOTAS_INICIALES; i++) {
            cargador.encolar(nuevaPelotaAleatoria());
        }
    }

    // Cada material tiene su densidad y su elasticidad
    private Pelota nuevaPelotaAleatoria() {
        switch (azar.nextInt(4)) {
            case 0:  return new Pelota(0, 0, RADIO, 1.0, 0.90); // goma
            case 1:  return new Pelota(0, 0, RADIO, 1.5, 0.60); // madera
            case 2:  return new Pelota(0, 0, RADIO, 2.2, 0.30); // piedra
            default: return new Pelota(0, 0, RADIO, 3.0, 0.45); // acero
        }
    }

    // Saca la primera pelota del cargador y la lanza hacia el mouse
    public void lanzar(double objetivoX, double objetivoY) {
        if (cargador.estaVacia() || terminada()) {
            return;
        }
        Pelota p = cargador.desencolar();
        p.setX(origenX - RADIO);
        p.setY(origenY - RADIO);
        apuntar(p, objetivoX, objetivoY);

        entidades.agregar(p);
        enVuelo.agregar(p);
    }

    // Más lejos del lanzador = más fuerza (hasta un máximo)
    private void apuntar(Pelota p, double objetivoX, double objetivoY) {
        double dx = objetivoX - origenX;
        double dy = objetivoY - origenY; // negativo = hacia arriba
        double distancia = Math.min(Math.sqrt(dx * dx + dy * dy), DISTANCIA_MAXIMA);
        p.lanzar(distancia * FACTOR_FUERZA, dx, dy);
    }

    // Puntitos que muestran por dónde iría la próxima pelota (sin contar los rebotes)
    public void dibujarTrayectoria(Graphics2D g, double objetivoX, double objetivoY) {
        if (cargador.estaVacia() || terminada()) {
            return;
        }
        Pelota proxima = cargador.frente();
        Pelota fantasma = new Pelota(origenX - RADIO, origenY - RADIO, RADIO,
                proxima.getDensidad(), proxima.getElasticidad());
        apuntar(fantasma, objetivoX, objetivoY);

        g.setColor(new Color(255, 230, 90, 170));
        for (int paso = 0; paso < 240; paso++) {
            fantasma.actualizar(1.0 / 120);
            double cx = fantasma.getX() + RADIO;
            double cy = fantasma.getY() + RADIO;
            if (cy > alto || cx < 0 || cx > ancho) {
                break;
            }
            if (paso % 6 == 0) {
                g.fillOval((int) cx - 2, (int) cy - 2, 4, 4);
            }
        }
    }

    public void actualizar(double dt) {
        // Polimorfismo: cada entidad se actualiza a su manera
        for (int i = 0; i < entidades.tamanio(); i++) {
            entidades.obtener(i).actualizar(dt);
        }
        resolverPelotas();
    }

    private void resolverPelotas() {
        int i = 0;
        while (i < enVuelo.tamanio()) {
            Pelota p = enVuelo.obtener(i);
            if (resolver(p)) {
                enVuelo.eliminar(i);
                entidades.eliminar(entidades.indiceDe(p));
            } else {
                i++;
            }
        }
    }

    // Devuelve true si la pelota sale del juego (acierto o pérdida)
    private boolean resolver(Pelota p) {
        rebotarEnParedes(p);
        rebotarEnObstaculos(p);

        // Emboca solo si entra por arriba (cayendo)
        if (p.getVy() > 0 && aro.choca(p)) {
            registrarAcierto(p);
            return true;
        }
        if (seDetuvoEnElSuelo(p)) {
            vidas--;
            return true;
        }
        return false;
    }

    private void rebotarEnParedes(Pelota p) {
        if (p.getX() < 0) {
            p.setX(0);
            p.rebotarHorizontal();
        } else if (p.getX() + p.getAncho() > ancho) {
            p.setX(ancho - p.getAncho());
            p.rebotarHorizontal();
        }
        if (p.getY() < 0) {
            p.setY(0);
            p.rebotarVertical();
        } else if (p.getY() + p.getAlto() > alto) {
            p.setY(alto - p.getAlto());
            p.rebotarVertical();
        }
    }

    private void rebotarEnObstaculos(Pelota p) {
        for (int i = 0; i < obstaculos.tamanio(); i++) {
            Obstaculo o = obstaculos.obtener(i);
            if (p.choca(o)) {
                rebotarContra(p, o);
            }
        }
    }

    // Se separa por el eje donde entró menos y rebota en ese eje
    private void rebotarContra(Pelota p, Obstaculo o) {
        double dx = (p.getX() + p.getAncho() / 2) - (o.getX() + o.getAncho() / 2);
        double dy = (p.getY() + p.getAlto() / 2) - (o.getY() + o.getAlto() / 2);
        double entraX = (p.getAncho() + o.getAncho()) / 2 - Math.abs(dx);
        double entraY = (p.getAlto() + o.getAlto()) / 2 - Math.abs(dy);

        if (entraX < entraY) {
            p.setX(p.getX() + (dx > 0 ? entraX : -entraX));
            p.rebotarHorizontal();
        } else {
            p.setY(p.getY() + (dy > 0 ? entraY : -entraY));
            p.rebotarVertical();
        }
    }

    // Una pelota con poca elasticidad se "muere" rápido en el piso
    private boolean seDetuvoEnElSuelo(Pelota p) {
        boolean enElSuelo = p.getY() + p.getAlto() >= alto - 1;
        return enElSuelo && Math.abs(p.getVy()) < 40;
    }

    private void registrarAcierto(Pelota p) {
        puntaje += PUNTOS_POR_ACIERTO;
        historial.apilar(new Acierto(PUNTOS_POR_ACIERTO, p.getDensidad(), p.getElasticidad()));
        // Dificultad creciente: el aro se acelera un 10%
        aro.setVelocidad(aro.getVx() * 1.1, 0);
    }

    public boolean terminada() {
        return vidas <= 0 || (cargador.estaVacia() && enVuelo.estaVacia());
    }

    public void dibujar(Graphics2D g) {
        g.setColor(new Color(60, 66, 85));
        g.drawLine(0, (int) alto - 1, (int) ancho, (int) alto - 1);
        for (int i = 0; i < entidades.tamanio(); i++) {
            entidades.obtener(i).dibujar(g);
        }
    }

    public double getOrigenX() { return origenX; }
    public double getOrigenY() { return origenY; }

    // ----- Estadisticas (lo que lee el HUD) -----
    @Override
    public int getPuntaje() { return puntaje; }

    @Override
    public int getVidas() { return vidas; }

    @Override
    public int getAciertos() { return historial.tamanio(); }

    @Override
    public int getPelotasEnCargador() { return cargador.tamanio(); }

    @Override
    public String getMensaje() {
        if (historial.estaVacia()) {
            return "Click para lanzar";
        }
        Acierto ultimo = historial.tope();
        return String.format("Último acierto: +%d (densidad %.1f, elasticidad %.2f)",
                ultimo.getPuntos(), ultimo.getDensidad(), ultimo.getElasticidad());
    }

    @Override
    public String getSiguiente() {
        if (cargador.estaVacia()) {
            return "Cargador vacío";
        }
        Pelota p = cargador.frente();
        return String.format("Siguiente: densidad %.1f, elasticidad %.2f",
                p.getDensidad(), p.getElasticidad());
    }
}
