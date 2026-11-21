package uabc.modelo;

import uabc.Alimento;
import uabc.Jugar;

// Mascota muñeca. Está feliz o aburrida según su felicidad y puede ponerse a llorar.
public class    Muneca extends Tamagotchi {

    // true si está llorando.
    private boolean estaLlorando;

    // Crea una muñeca que no está llorando.
    public Muneca(String nombre) {
        super(nombre);
        this.estaLlorando = false;
    }

    // Dibujo de la muñeca.
    @Override
    public String obtenDraw() {
        return "[Muneca]";
    }

    // Agrega a la tarjeta si está feliz o aburrida (feliz con 50 o más de felicidad) y si llora.
    @Override
    public String toString() {
        return super.toString() + "\nDetalles: Estado: " + (felicidad >= 50 ? "Feliz" : "Aburrida")
                + " | Llorando: " + (estaLlorando ? "Si" : "No");
    }

    // Al comer gana 5 de salud, pero solo 8 de felicidad.
    @Override
    protected void aplicarBonoAlimento() {
        setSalud(salud + 5);
        setFelicidad(felicidad + 8);
    }

    // Jugar le da menos hambre (10).
    @Override
    protected int hambrePorJuego() {
        return 10;
    }

    // Deja de llorar cuando la alimentan o juegan con ella.
    @Override
    protected void alAtender() {
        this.estaLlorando = false;
    }

    // Energía que le da cada comida a la muñeca (la gasolina le da mucha).
    @Override
    protected int energiaPorAlimento(Alimento alimento) {
        return switch (alimento) {
            case CROQUETAS -> 8;
            case MANZANA -> 12;
            case GASOLINA -> 30;
        };
    }

    // Felicidad que le da cada juego a la muñeca (le gusta el escondite).
    @Override
    protected int felicidadPorJuego(Jugar juego) {
        return switch (juego) {
            case PELOTA -> 15;
            case ESCONDITE -> 35;
            case CUERDA -> 20;
        };
    }

    // Energía que le gasta cada juego (siempre 10).
    @Override
    protected int energiaPorJuego(Jugar juego) {
        return switch (juego) {
            case PELOTA -> 10;
            case ESCONDITE -> 10;
            case CUERDA -> 10;
        };
    }

    // Mensaje de cómo juega la muñeca.
    @Override
    protected String describirJuego(Jugar juego) {
        return switch (juego) {
            case PELOTA -> nombre + " rodo la pelota despacito.";
            case ESCONDITE -> nombre + " se escondio y se rio bajito.";
            case CUERDA -> nombre + " jugo con la cuerda entre sus manitas.";
        };
    }

    // Si su felicidad baja de 40, se pone a llorar.
    @Override
    protected void alPasarTiempo() {
        if (felicidad < 40) {
            this.estaLlorando = true;
        }
    }

    // Una muñeca muerta deja de llorar.
    @Override
    protected void alMorir() {
        this.estaLlorando = false;
    }

    // Nombres de sus acciones especiales.
    @Override
    public String[] getNombresAccionesEspeciales() {
        return new String[]{"Expresar emocion", "Llorar"};
    }

    // Ejecuta la acción especial: 0 expresar emoción, 1 llorar.
    // Si está muerta no hace nada y otro número lanza error.
    @Override
    public String ejecutarAccionEspecial(int indice) {
        if (estaMuerto()) {
            return mensajeMuerto();
        }
        return switch (indice) {
            case 0 -> expresarEmocion();
            case 1 -> llorar();
            default -> throw new IllegalArgumentException("Accion invalida: " + indice);
        };
    }

    // Dice si está feliz o aburrida.
    public String expresarEmocion() {
        return nombre + (felicidad >= 50 ? " esta feliz." : " esta aburrida.");
    }

    // Se pone a llorar y pierde 5 de felicidad.
    public String llorar() {
        this.estaLlorando = true;
        setFelicidad(felicidad - 5);
        return nombre + " esta llorando.";
    }
}
