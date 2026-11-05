package juego.reglas;

import motor.Escena;

// Contrato: qué escena se muestra cuando termina la partida.
// Así "reglas" no depende de "pantallas" (evita dependencias circulares).
public interface AlTerminar {
    Escena crearEscenaFin(int puntaje, int aciertos);
}
