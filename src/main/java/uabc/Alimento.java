package uabc;
// Comidas disponibles, con el hambre que quitan y la energía que dan.
public enum Alimento {

    // Quita 30 de hambre y da 15 de energía.
    CROQUETAS("Croquetas", 30, 15),

    // Quita 15 de hambre y da 10 de energía.
    MANZANA("Manzana", 15, 10),

    // Quita 10 de hambre y da 25 de energía.
    GASOLINA("Gasolina", 10, 25);

    // Nombre que ve el usuario.
    public final String nombre;

    // Puntos de hambre que se restan al comer.
    public final int reduccionHambre;

    // Puntos de energía que se suman (cada especie puede cambiarlos).
    public final int energiaBase;

    // Guarda los datos de cada comida.
    Alimento(String nombre, int reduccionHambre, int energiaBase) {
        this.nombre = nombre;
        this.reduccionHambre = reduccionHambre;
        this.energiaBase = energiaBase;
    }
}
