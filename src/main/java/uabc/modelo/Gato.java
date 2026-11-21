package uabc.modelo;

import uabc.Alimento;
import uabc.Jugar;

// Mascota gato. Además de lo común, tiene pereza y puede estar ronroneando.
public class Gato extends Tamagotchi {

    // Qué tan perezoso está (0 a 100).
    private int nivelPereza;

    // true si está ronroneando.
    private boolean ronroneando;

    // Crea un gato con pereza 50 y sin ronronear.
    public Gato(String nombre) {
        super(nombre);
        this.nivelPereza = 50;
        this.ronroneando = false;
    }

    // Dibujo del gato.
    @Override
    public String obtenDraw() {
        return "[Gato]";
    }

    // Agrega los detalles del gato a la tarjeta de estado.
    @Override
    public String toString() {
        return super.toString() + "\nDetalles: Pereza: " + nivelPereza
                + " | Ronroneando: " + (ronroneando ? "Si" : "No");
    }

    // Al dormir recupera 15 de energía extra y le sube la pereza (+10).
    @Override
    protected void aplicarBonoDormir() {
        setEnergia(energia + 15);
        this.nivelPereza = limitarRango(this.nivelPereza + 10);
    }

    // Energía que le da cada comida al gato.
    @Override
    protected int energiaPorAlimento(Alimento alimento) {
        return switch (alimento) {
            case CROQUETAS -> 20;
            case MANZANA -> 5;
            case GASOLINA -> 10;
        };
    }

    // Felicidad que le da cada juego al gato (le gusta más la cuerda).
    @Override
    protected int felicidadPorJuego(Jugar juego) {
        return switch (juego) {
            case PELOTA -> 20;
            case ESCONDITE -> 25;
            case CUERDA -> 40;
        };
    }

    // Energía que le gasta cada juego al gato.
    @Override
    protected int energiaPorJuego(Jugar juego) {
        return switch (juego) {
            case PELOTA -> 15;
            case ESCONDITE -> 15;
            case CUERDA -> 20;
        };
    }

    // Mensaje de cómo juega el gato.
    @Override
    protected String describirJuego(Jugar juego) {
        return switch (juego) {
            case PELOTA -> nombre + " empujo la pelota con la pata.";
            case ESCONDITE -> nombre + " se escondio detras del sillon.";
            case CUERDA -> nombre + " salto tras la cuerda.";
        };
    }

    // Un gato muerto deja de ronronear.
    @Override
    protected void alMorir() {
        this.ronroneando = false;
    }

    // Nombres de sus acciones especiales.
    @Override
    public String[] getNombresAccionesEspeciales() {
        return new String[]{"Ronronear", "Maullar"};
    }

    // Ejecuta la acción especial: 0 ronronear, 1 maullar.
    // Si está muerto no hace nada y otro número lanza error.
    @Override
    public String ejecutarAccionEspecial(int indice) {
        if (estaMuerto()) {
            return mensajeMuerto();
        }
        return switch (indice) {
            case 0 -> ronronear();
            case 1 -> maullar();
            default -> throw new IllegalArgumentException("Accion invalida: " + indice);
        };
    }

    // Empieza a ronronear.
    public String ronronear() {
        this.ronroneando = true;
        return nombre + " esta ronroneando.";
    }

    // Maúlla y deja de ronronear.
    public String maullar() {
        this.ronroneando = false;
        return nombre + " esta maullando.";
    }
}
