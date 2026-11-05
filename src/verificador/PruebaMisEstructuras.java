package verificador;

import estructuras.MiCola;
import estructuras.MiLista;
import estructuras.MiPila;

public class PruebaMisEstructuras {

    // Punto de entrada
    public static void main(String[] args) {

        VerificadorEstructuras.verificarLista(new MiLista<Integer>());

        VerificadorEstructuras.verificarPila(new MiPila<Integer>());

        VerificadorEstructuras.verificarCola(new MiCola<Integer>());
    }
}
