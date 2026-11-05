package juego;

import juego.pantallas.CreadorEscenaFin;
import juego.pantallas.EscenaMenu;
import juego.pantallas.Hud;
import juego.reglas.EscenaPartida;
import juego.reglas.Mundo;
import motor.Ventana;

public class Main {

    public static final int ANCHO = 900;
    public static final int ALTO = 600;

    public static void main(String[] args) {
        Mundo mundo = new Mundo(ANCHO, ALTO);
        Hud hud = new Hud(mundo, ANCHO, ALTO);
        CreadorEscenaFin fin = new CreadorEscenaFin();
        EscenaPartida partida = new EscenaPartida(mundo, hud, fin);
        EscenaMenu menu = new EscenaMenu(partida);
        fin.setMenu(menu);

        Ventana.abrir("Laboratorio de rebote", ANCHO, ALTO, menu);
    }
}
