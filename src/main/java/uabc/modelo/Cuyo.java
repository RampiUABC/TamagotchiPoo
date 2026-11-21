package uabc.modelo;

// Mascota cuyo. Lleva la cuenta de sus chillidos y de la madera que ha roído.
public class Cuyo extends Tamagotchi {

    // Chillidos (0 a 100). Suben con cada "cuy cuy".
    private int nivelChillidos;

    // Madera roída (0 a 100). Sube cada vez que roe.
    private int maderaRoida;

    // Crea un cuyo con chillidos 20 y sin madera roída.
    public Cuyo(String nombre) {
        super(nombre);
        this.nivelChillidos = 20;
        this.maderaRoida = 0;
    }

    // Dibujo del cuyo.
    @Override
    public String obtenDraw() {
        return "[Cuyo]";
    }

    // Agrega los chillidos y la madera roída a la tarjeta de estado.
    @Override
    public String toString() {
        return super.toString() + "\nDetalles: Chillidos: " + nivelChillidos
                + " | Madera roida: " + maderaRoida;
    }

    // Nombres de sus acciones especiales.
    @Override
    public String[] getNombresAccionesEspeciales() {
        return new String[]{"Cuy cuy", "Roer madera"};
    }

    // Ejecuta la acción especial: 0 cuy cuy, 1 roer madera.
    // Si está muerto no hace nada y otro número lanza error.
    @Override
    public String ejecutarAccionEspecial(int indice) {
        if (estaMuerto()) {
            return mensajeMuerto();
        }
        return switch (indice) {
            case 0 -> cuyCuy();
            case 1 -> roerMadera();
            default -> throw new IllegalArgumentException("Accion invalida: " + indice);
        };
    }

    // Hace "cuy cuy": sube los chillidos (+5).
    public String cuyCuy() {
        nivelChillidos = limitarRango(nivelChillidos + 5);
        return nombre + " hace: cuy cuy.";
    }

    // Roe madera: +10 madera roída, +5 de felicidad y -3 de energía.
    public String roerMadera() {
        maderaRoida = limitarRango(maderaRoida + 10);
        setFelicidad(felicidad + 5);
        setEnergia(energia - 3);
        return nombre + " esta royendo madera.";
    }
}
