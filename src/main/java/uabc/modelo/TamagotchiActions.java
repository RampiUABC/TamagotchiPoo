package uabc.modelo;

import uabc.Alimento;
import uabc.Jugar;

// Acciones que toda mascota debe poder hacer.
// Las tres primeras son iguales para todas; las dos especiales cambian según la especie.
public interface TamagotchiActions {

    // Da de comer a la mascota y devuelve un mensaje de lo ocurrido.
    String alimentar(Alimento alimento);

    // Juega con la mascota y devuelve un mensaje de lo ocurrido.
    String jugar(Jugar juego);

    // Manda a dormir a la mascota y devuelve un mensaje de lo ocurrido.
    String dormir();

    // Devuelve los nombres de las 2 acciones especiales de la especie.
    String[] getNombresAccionesEspeciales();

    // Ejecuta una acción especial (0 la primera, 1 la segunda). Otro número lanza error.
    String ejecutarAccionEspecial(int indice);

}
