package uabc.modelo;

import java.util.ArrayList;
import java.util.List;

// Guarda todas las mascotas adoptadas, en el orden en que llegaron.
public class Coleccion {

    // Lista de mascotas.
    private ArrayList<Tamagotchi> misJuguetes;

    // Crea la colección vacía.
    public Coleccion() {
        misJuguetes = new ArrayList<>();
    }

    // Agrega una mascota al final de la lista.
    public void agregar(Tamagotchi t) {
        misJuguetes.add(t);
    }

    // Devuelve la lista de mascotas.
    public List<Tamagotchi> getMisJuguetes() {
        return misJuguetes;
    }

    // Devuelve la mascota de esa posición (empieza en 0).
    public Tamagotchi obtener(int indice) {
        return misJuguetes.get(indice);
    }

    // Dice si todavía no hay mascotas.
    public boolean estaVacia() {
        return misJuguetes.isEmpty();
    }
}
