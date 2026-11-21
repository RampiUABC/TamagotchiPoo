package uabc.modelo;

import uabc.Alimento;
import uabc.Jugar;

// Mascota perro. Además de lo común, tiene ladridos, hiperactividad y agresividad (0 a 100).
public class Perro extends Tamagotchi {

    // Cuánto ha ladrado.
    private int nivelLadridos;

    // Qué tan hiperactivo está.
    private int nivelHiperactividad;

    // Qué tan agresivo está.
    private int nivelAgresividad;

    // Crea un perro con ladridos 10, hiperactividad 70 y agresividad 20.
    public Perro(String nombre) {
        super(nombre);
        this.nivelLadridos = 10;
        this.nivelHiperactividad = 70;
        this.nivelAgresividad = 20;
    }

    // Dibujo del perro.
    @Override
    public String obtenDraw() {
        return "[Perro]";
    }

    // Agrega los detalles del perro a la tarjeta de estado.
    @Override
    public String toString() {
        return super.toString() + "\nDetalles: Ladridos: " + nivelLadridos + " | Hiperactividad: " + nivelHiperactividad
                + " | Agresividad: " + nivelAgresividad;
    }

    // Al dormir se calma: bajan ladridos, hiperactividad y agresividad.
    @Override
    protected void aplicarBonoDormir() {
        nivelLadridos = limitarRango(nivelLadridos - 10);
        nivelHiperactividad = limitarRango(nivelHiperactividad - 10);
        nivelAgresividad = limitarRango(nivelAgresividad - 5);
    }

    // Energía que le da cada comida al perro (las croquetas le sirven más).
    @Override
    protected int energiaPorAlimento(Alimento alimento) {
        return switch (alimento) {
            case CROQUETAS -> 25;
            case MANZANA -> 8;
            case GASOLINA -> 12;
        };
    }

    // Felicidad que le da cada juego al perro (le encanta la pelota).
    @Override
    protected int felicidadPorJuego(Jugar juego) {
        return switch (juego) {
            case PELOTA -> 35;
            case ESCONDITE -> 15;
            case CUERDA -> 30;
        };
    }

    // Energía que le gasta cada juego al perro.
    @Override
    protected int energiaPorJuego(Jugar juego) {
        return switch (juego) {
            case PELOTA -> 25;
            case ESCONDITE -> 10;
            case CUERDA -> 20;
        };
    }

    // Mensaje de cómo juega el perro.
    @Override
    protected String describirJuego(Jugar juego) {
        return switch (juego) {
            case PELOTA -> nombre + " corrio tras la pelota.";
            case ESCONDITE -> nombre + " busco por todos lados en el escondite.";
            case CUERDA -> nombre + " jalo la cuerda con todas sus fuerzas.";
        };
    }

    // Nombres de sus acciones especiales.
    @Override
    public String[] getNombresAccionesEspeciales() {
        return new String[]{"Ladrar", "Perseguir cola"};
    }

    // Ejecuta la acción especial: 0 ladrar, 1 perseguir cola.
    // Si está muerto no hace nada y otro número lanza error.
    @Override
    public String ejecutarAccionEspecial(int indice) {
        if (estaMuerto()) {
            return mensajeMuerto();
        }
        return switch (indice) {
            case 0 -> ladrar();
            case 1 -> perseguirCola();
            default -> throw new IllegalArgumentException("Accion invalida: " + indice);
        };
    }

    // Ladra: sube los ladridos (+10) y la agresividad (+5).
    public String ladrar() {
        nivelLadridos = limitarRango(nivelLadridos + 10);
        nivelAgresividad = limitarRango(nivelAgresividad + 5);
        return nombre + " esta ladrando.";
    }

    // Persigue su cola: sube la hiperactividad (+5) y gasta 5 de energía.
    public String perseguirCola() {
        nivelHiperactividad = limitarRango(nivelHiperactividad + 5);
        setEnergia(energia - 5);
        return nombre + " esta persiguiendo su cola.";
    }
}
