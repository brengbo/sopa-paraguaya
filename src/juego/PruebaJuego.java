package juego;

import java.awt.event.KeyEvent;
import java.util.Random;

import juego.pantallas.CreadorEscenaFin;
import juego.pantallas.EscenaFin;
import juego.pantallas.EscenaMenu;
import juego.pantallas.Hud;
import juego.reglas.EscenaPartida;
import juego.reglas.Mundo;
import motor.Entrada;
import motor.Motor;

// Prueba sin ventana: menu -> partida -> fin -> menú, simulando teclado y mouse
public class PruebaJuego {

    public static void main(String[] args) {
        Mundo mundo = new Mundo(Main.ANCHO, Main.ALTO);
        Hud hud = new Hud(mundo, Main.ANCHO, Main.ALTO);
        CreadorEscenaFin fin = new CreadorEscenaFin();
        EscenaPartida partida = new EscenaPartida(mundo, hud, fin);
        EscenaMenu menu = new EscenaMenu(partida);
        fin.setMenu(menu);

        Motor motor = new Motor(Main.ANCHO, Main.ALTO);
        Entrada entrada = motor.getEntrada();
        motor.iniciar(menu);

        verificar("arranca en el menu", motor.getEscenaActual() == menu);

        entrada.presionarTecla(KeyEvent.VK_ENTER);
        motor.paso();
        entrada.soltarTecla(KeyEvent.VK_ENTER);
        motor.paso();
        verificar("ENTER lleva a la partida", motor.getEscenaActual() == partida);

        Random azar = new Random(7);
        int pasos = 0;
        while (motor.getEscenaActual() == partida && pasos < 120 * 600) {
            if (pasos % 240 == 0) {
                entrada.moverMouse(300 + azar.nextInt(500), 60 + azar.nextInt(400));
                entrada.apretarMouse();
                entrada.soltarMouse();
            }
            motor.paso();
            pasos++;
        }
        verificar("la partida termina sola", motor.getEscenaActual() instanceof EscenaFin);

        entrada.presionarTecla(KeyEvent.VK_ENTER);
        motor.paso();
        entrada.soltarTecla(KeyEvent.VK_ENTER);
        motor.paso();
        verificar("ENTER vuelve al menu", motor.getEscenaActual() == menu);

        System.out.println("Pruebas del juego: OK");
    }

    private static void verificar(String descripcion, boolean ok) {
        if (!ok) {
            throw new IllegalStateException("FALLÓ: " + descripcion);
        }
        System.out.println("  ok - " + descripcion);
    }
}
