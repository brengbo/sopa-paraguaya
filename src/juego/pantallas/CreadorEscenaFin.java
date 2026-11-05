package juego.pantallas;

import juego.reglas.AlTerminar;
import motor.Escena;

public class CreadorEscenaFin implements AlTerminar {

    private Escena menu;

    // Se completa después de armar el menú (el menú ya conoce a la partida)
    public void setMenu(Escena menu) {
        if (menu == null) {
            throw new IllegalArgumentException("Falta el menú");
        }
        this.menu = menu;
    }

    @Override
    public Escena crearEscenaFin(int puntaje, int aciertos) {
        if (menu == null) {
            throw new IllegalStateException("Falta llamar a setMenu antes de jugar");
        }
        return new EscenaFin(puntaje, aciertos, menu);
    }
}
