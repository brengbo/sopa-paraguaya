package estructuras;

public class MiLista<T> implements Lista<T> {

    private static class Nodo<T> {
        T dato;
        Nodo<T> siguiente;
        Nodo(T dato) { this.dato = dato; }
    }

    private Nodo<T> primero;
    private Nodo<T> ultimo;
    private int cantidad;

    @Override
    public void agregar(T elemento) {
        Nodo<T> nuevo = new Nodo<>(elemento);
        if (primero == null) {
            primero = nuevo;
        } else {
            ultimo.siguiente = nuevo;
        }
        ultimo = nuevo;
        cantidad++;
    }

    @Override
    public void insertar(int indice, T elemento) {
        if (indice < 0 || indice > cantidad) {
            throw new IndexOutOfBoundsException("Índice inválido: " + indice);
        }
        if (indice == cantidad) {
            agregar(elemento);
            return;
        }
        Nodo<T> nuevo = new Nodo<>(elemento);
        if (indice == 0) {
            nuevo.siguiente = primero;
            primero = nuevo;
        } else {
            Nodo<T> anterior = nodoEn(indice - 1);
            nuevo.siguiente = anterior.siguiente;
            anterior.siguiente = nuevo;
        }
        cantidad++;
    }

    @Override
    public T obtener(int indice) {
        validar(indice);
        return nodoEn(indice).dato;
    }

    @Override
    public int indiceDe(T elemento) {
        Nodo<T> actual = primero;
        int i = 0;
        while (actual != null) {
            boolean iguales = (actual.dato == null)
                    ? elemento == null
                    : actual.dato.equals(elemento);
            if (iguales) {
                return i;
            }
            actual = actual.siguiente;
            i++;
        }
        return -1;
    }

    @Override
    public int tamanio() {
        return cantidad;
    }

    @Override
    public boolean estaVacia() {
        return cantidad == 0;
    }

    @Override
    public T eliminar(int indice) {
        validar(indice);
        T dato;
        if (indice == 0) {
            dato = primero.dato;
            primero = primero.siguiente;
            if (primero == null) {
                ultimo = null;
            }
        } else {
            Nodo<T> anterior = nodoEn(indice - 1);
            Nodo<T> borrar = anterior.siguiente;
            dato = borrar.dato;
            anterior.siguiente = borrar.siguiente;
            if (borrar == ultimo) {
                ultimo = anterior;
            }
        }
        cantidad--;
        return dato;
    }

    @Override
    public void vaciar() {
        primero = null;
        ultimo = null;
        cantidad = 0;
    }

    // Auxiliares
    private void validar(int indice) {
        if (indice < 0 || indice >= cantidad) {
            throw new IndexOutOfBoundsException("Índice inválido: " + indice);
        }
    }

    private Nodo<T> nodoEn(int indice) {
        Nodo<T> actual = primero;
        for (int i = 0; i < indice; i++) {
            actual = actual.siguiente;
        }
        return actual;
    }
}
