package uabc.modelo;

import uabc.Alimento;
import uabc.Jugar;

// Mascota Sigilyph. Tiene tipo, habilidad, poder y dobla cucharas con telekinesis.
public class Sigilyph extends Tamagotchi {

    // Tipo elemental (Psíquico/Volador).
    private final String tipoElemental;

    // Habilidad especial.
    private final String habilidad;

    // Poder (0 a 100).
    private int nivelPoder;

    // Cuántas cucharas ha doblado.
    private int cucharasDobladas;

    // Crea un Sigilyph con poder 60 y sin cucharas dobladas.
    public Sigilyph(String nombre) {
        super(nombre);
        this.tipoElemental = "Psiquico/Volador";
        this.habilidad = "Muro Magico";
        this.nivelPoder = 60;
        this.cucharasDobladas = 0;
    }

    // Dibujo del Sigilyph.
    @Override
    public String obtenDraw() {
        return "[Sigilyph]";
    }

    // Agrega tipo, poder, habilidad y cucharas dobladas a la tarjeta de estado.
    @Override
    public String toString() {
        return super.toString() + "\nDetalles: Tipo: " + tipoElemental + " | Poder: " + nivelPoder
                + " | Habilidad: " + habilidad + " | Cucharas dobladas: " + cucharasDobladas;
    }

    // Energía que le da cada comida al Sigilyph (le sirve más la manzana).
    @Override
    protected int energiaPorAlimento(Alimento alimento) {
        return switch (alimento) {
            case CROQUETAS -> 10;
            case MANZANA -> 20;
            case GASOLINA -> 15;
        };
    }

    // Felicidad que le da cada juego al Sigilyph.
    @Override
    protected int felicidadPorJuego(Jugar juego) {
        return switch (juego) {
            case PELOTA -> 25;
            case ESCONDITE -> 30;
            case CUERDA -> 15;
        };
    }

    // Energía que le gasta cada juego al Sigilyph.
    @Override
    protected int energiaPorJuego(Jugar juego) {
        return switch (juego) {
            case PELOTA -> 10;
            case ESCONDITE -> 15;
            case CUERDA -> 10;
        };
    }

    // Mensaje de cómo juega el Sigilyph.
    @Override
    protected String describirJuego(Jugar juego) {
        return switch (juego) {
            case PELOTA -> nombre + " floto alrededor de la pelota.";
            case ESCONDITE -> nombre + " se escondio detras de su Muro Magico.";
            case CUERDA -> nombre + " bailo en el aire con la cuerda.";
        };
    }

    // Nombres de sus acciones especiales.
    @Override
    public String[] getNombresAccionesEspeciales() {
        return new String[]{"Entrenar", "Doblar cuchara"};
    }

    // Ejecuta la acción especial: 0 entrenar, 1 doblar cuchara.
    // Si está muerto no hace nada y otro número lanza error.
    @Override
    public String ejecutarAccionEspecial(int indice) {
        if (estaMuerto()) {
            return mensajeMuerto();
        }
        return switch (indice) {
            case 0 -> entrenar();
            case 1 -> doblarCuchara();
            default -> throw new IllegalArgumentException("Accion invalida: " + indice);
        };
    }

    // Entrena: sube el poder (+5).
    public String entrenar() {
        this.nivelPoder = limitarRango(this.nivelPoder + 5);
        return nombre + " esta entrenando.";
    }

    // Dobla una cuchara: suma una al contador y gasta 5 de energía.
    public String doblarCuchara() {
        cucharasDobladas++;
        setEnergia(energia - 5);
        return nombre + " doblo una cuchara con telekinesis.";
    }
}
