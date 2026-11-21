package uabc.modelo;

import uabc.Alimento;
import uabc.Jugar;

// Mascota Ditto. Tiene tipo, poder y puede transformarse en otras formas.
public class Ditto extends Tamagotchi {

    // Formas en las que se puede transformar.
    private static final String[] FORMAS = {"Ditto", "Pikachu", "Eevee", "Charmander", "Squirtle"};

    // Tipo elemental (siempre Normal).
    private final String tipoElemental;

    // Poder (0 a 100).
    private int nivelPoder;

    // Posición de su forma actual en FORMAS.
    private int indiceForma;

    // Crea un Ditto tipo Normal, con poder 50 y en su forma original.
    public Ditto(String nombre) {
        super(nombre);
        this.tipoElemental = "Normal";
        this.nivelPoder = 50;
        this.indiceForma = 0;
    }

    // Dibujo del Ditto.
    @Override
    public String obtenDraw() {
        return "[Ditto]";
    }

    // Agrega tipo, poder y forma actual a la tarjeta de estado.
    @Override
    public String toString() {
        return super.toString() + "\nDetalles: Tipo: " + tipoElemental + " | Poder: " + nivelPoder
                + " | Forma: " + FORMAS[indiceForma];
    }

    // Energía que le da cada comida al Ditto.
    @Override
    protected int energiaPorAlimento(Alimento alimento) {
        return switch (alimento) {
            case CROQUETAS -> 12;
            case MANZANA -> 18;
            case GASOLINA -> 12;
        };
    }

    // Felicidad que le da cada juego al Ditto.
    @Override
    protected int felicidadPorJuego(Jugar juego) {
        return switch (juego) {
            case PELOTA -> 30;
            case ESCONDITE -> 20;
            case CUERDA -> 25;
        };
    }

    // Energía que le gasta cada juego al Ditto.
    @Override
    protected int energiaPorJuego(Jugar juego) {
        return switch (juego) {
            case PELOTA -> 20;
            case ESCONDITE -> 15;
            case CUERDA -> 15;
        };
    }

    // Mensaje de cómo juega el Ditto.
    @Override
    protected String describirJuego(Jugar juego) {
        return switch (juego) {
            case PELOTA -> nombre + " se hizo bolita y reboto como pelota.";
            case ESCONDITE -> nombre + " se transformo en piedra para esconderse.";
            case CUERDA -> nombre + " se estiro como una cuerda.";
        };
    }

    // Nombres de sus acciones especiales.
    @Override
    public String[] getNombresAccionesEspeciales() {
        return new String[]{"Transformarse", "Entrenar"};
    }

    // Ejecuta la acción especial: 0 transformarse, 1 entrenar.
    // Si está muerto no hace nada y otro número lanza error.
    @Override
    public String ejecutarAccionEspecial(int indice) {
        if (estaMuerto()) {
            return mensajeMuerto();
        }
        return switch (indice) {
            case 0 -> transformarse();
            case 1 -> entrenar();
            default -> throw new IllegalArgumentException("Accion invalida: " + indice);
        };
    }

    // Pasa a la siguiente forma (al final de la lista vuelve a la primera) y gasta 5 de energía.
    public String transformarse() {
        indiceForma = (indiceForma + 1) % FORMAS.length;
        setEnergia(energia - 5);
        return nombre + " se transformo en " + FORMAS[indiceForma] + ".";
    }

    // Entrena: sube el poder (+5).
    public String entrenar() {
        this.nivelPoder = limitarRango(this.nivelPoder + 5);
        return nombre + " esta entrenando.";
    }
}
