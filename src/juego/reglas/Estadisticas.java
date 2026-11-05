package juego.reglas;

// Datos que el HUD necesita mostrar (lo implementa el Mundo)
public interface Estadisticas {
    int getPuntaje();
    int getVidas();
    int getAciertos();
    int getPelotasEnCargador();
    String getMensaje();
    String getSiguiente();
}
