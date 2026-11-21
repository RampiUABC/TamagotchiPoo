package uabc;
// Juegos disponibles, con la felicidad que dan y la energía que cuestan.
public enum Jugar {

    // Da 25 de felicidad y cuesta 20 de energía.
    PELOTA("Jugar con Pelota", 25, 20),

    // Da 20 de felicidad y cuesta 15 de energía.
    ESCONDITE("Jugar al Escondite", 20, 15),

    // Da 30 de felicidad y cuesta 25 de energía.
    CUERDA("Jalar la Cuerda", 30, 25);

    // Nombre que ve el usuario.
    public final String nombre;

    // Puntos de felicidad que se suman al jugar.
    public final int aumentoFelicidad;

    // Puntos de energía que se restan al jugar.
    public final int gastoEnergetico;

    // Guarda los datos de cada juego.
    Jugar(String nombre, int aumentoFelicidad, int gastoEnergetico) {
        this.nombre = nombre;
        this.aumentoFelicidad = aumentoFelicidad;
        this.gastoEnergetico = gastoEnergetico;
    }
}
