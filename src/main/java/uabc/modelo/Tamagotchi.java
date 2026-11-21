package uabc.modelo;

import uabc.Alimento;
import uabc.Jugar;

// Clase base de todas las mascotas. Guarda hambre, energía, felicidad y salud (de 0 a 100)
// y define cómo comer, jugar y dormir. Cada especie cambia lo que necesite sobrescribiendo
// los métodos "gancho" (los que dicen "Por defecto..."). Ojo: en hambre, mientras más alto es peor.
public abstract class Tamagotchi implements TamagotchiActions {

    // Nombre que le puso el usuario.
    protected String nombre;

    // Hambre (0 a 100). Más alto = más hambre.
    protected int hambre;

    // Energía (0 a 100).
    protected int energia;

    // Felicidad (0 a 100).
    protected int felicidad;

    // Salud (0 a 100). Si llega a 0 la mascota muere.
    protected int salud;

    // true si está dormida. Se quita al alimentarla o jugar con ella.
    protected boolean durmiendo;

    // true si ya murió. No revive.
    protected boolean muerto;

    // Crea la mascota con hambre 30, energía 80, felicidad 70 y salud 100.
    public Tamagotchi(String nombre) {
        this.nombre = nombre;
        this.hambre = 30;
        this.energia = 80;
        this.felicidad = 70;
        this.salud = 100;
        this.durmiendo = false;
        this.muerto = false;
    }

    // Cambia el hambre sin salirse de 0 a 100.
    public void setHambre(int hambre) {
        this.hambre = limitarRango(hambre);
    }

    // Cambia la energía sin salirse de 0 a 100.
    public void setEnergia(int energia) {
        this.energia = limitarRango(energia);
    }

    // Cambia la felicidad sin salirse de 0 a 100.
    public void setFelicidad(int felicidad) {
        this.felicidad = limitarRango(felicidad);
    }

    // Cambia la salud sin salirse de 0 a 100. Si queda en 0, la mascota muere.
    public void setSalud(int salud) {
        this.salud = limitarRango(salud);
        if (this.salud == 0) {
            morir();
        }
    }

    // Devuelve el nombre.
    public String getNombre() {
        return nombre;
    }

    // Devuelve el hambre.
    public int getHambre() {
        return hambre;
    }

    // Devuelve la energía.
    public int getEnergia() {
        return energia;
    }

    // Devuelve la felicidad.
    public int getFelicidad() {
        return felicidad;
    }

    // Devuelve la salud.
    public int getSalud() {
        return salud;
    }

    // Dice si está dormida.
    public boolean estaDurmiendo() {
        return durmiendo;
    }

    // Dice si está muerta.
    public boolean estaMuerto() {
        return muerto;
    }

    // Mantiene un número entre 0 y 100 (ejemplo: 120 pasa a 100 y -5 pasa a 0).
    protected int limitarRango(int valor) {
        return Math.max(0, Math.min(100, valor));
    }

    // Calcula el ánimo según los indicadores.
    // Revisa de lo más grave a lo menos grave: muerto, crítico, requiere atención, excelente o normal.
    public Animo obtenerEstadoAnimo() {
        if (muerto) {
            return Animo.MUERTO;
        }
        if (salud < 30 || hambre > 85 || energia < 15) {
            return Animo.CRITICO;
        }
        if (salud < 60 || felicidad < 40 || hambre > 65 || energia < 35) {
            return Animo.REQUIERE_ATENCION;
        }
        if (salud > 80 && energia >= 60 && hambre <= 30) {
            return Animo.EXCELENTE;
        }
        return Animo.NORMAL;
    }

    // Alimenta a la mascota: la despierta, baja el hambre, sube la energía y aplica los bonos.
    // Si está muerta no hace nada.
    @Override
    public String alimentar(Alimento alimento) {
        if (muerto) {
            return mensajeMuerto();
        }
        String aviso = despertarSiDuerme();
        setHambre(hambre - alimento.reduccionHambre);
        setEnergia(energia + energiaPorAlimento(alimento));
        aplicarBonoAlimento();
        alAtender();
        actualizarSalud();
        return conAvisoMuerte(aviso + nombre + " comio " + alimento.nombre + ".");
    }

    // Juega con la mascota: la despierta, sube la felicidad, gasta energía y da hambre.
    // Si está muerta no hace nada.
    @Override
    public String jugar(Jugar juego) {
        if (muerto) {
            return mensajeMuerto();
        }
        String aviso = despertarSiDuerme();
        setFelicidad(felicidad + felicidadPorJuego(juego));
        setEnergia(energia - energiaPorJuego(juego));
        setHambre(hambre + hambrePorJuego());
        alAtender();
        actualizarSalud();
        return conAvisoMuerte(aviso + describirJuego(juego));
    }

    // Manda a dormir: +40 de energía más el bono de la especie. No cambia el hambre.
    // Si está muerta no hace nada.
    @Override
    public String dormir() {
        if (muerto) {
            return mensajeMuerto();
        }
        durmiendo = true;
        setEnergia(energia + 40);
        aplicarBonoDormir();
        actualizarSalud();
        return conAvisoMuerte(nombre + " se ha puesto a dormir.");
    }

    // Pasa una hora. Despierta: sube el hambre y bajan la energía y la felicidad.
    // Dormida: sube poco el hambre y recupera energía. Si está descuidada pierde salud.
    // Devuelve un aviso si murió en esta hora, o texto vacío.
    public String pasarTiempo() {
        if (muerto) {
            return "";
        }
        if (durmiendo) {
            setHambre(hambre + 2);
            setEnergia(energia + 8);
        } else {
            setHambre(hambre + 4);
            setEnergia(energia - 3);
            setFelicidad(felicidad - 3);
        }
        alPasarTiempo();
        descuidar(2, 1);
        return muerto ? nombre + " ha muerto por falta de cuidados." : "";
    }

    // Bono extra al comer: +5 de salud y +10 de felicidad.
    protected void aplicarBonoAlimento() {
        setSalud(salud + 5);
        setFelicidad(felicidad + 10);
    }

    // Hambre que da jugar (15 por defecto).
    protected int hambrePorJuego() {
        return 15;
    }

    // Se ejecuta al alimentar o jugar. Por defecto no hace nada.
    protected void alAtender() {
    }

    // Bono extra al dormir. Por defecto no hace nada.
    protected void aplicarBonoDormir() {
    }

    // Energía que da cada comida a esta especie (por defecto la del alimento).
    protected int energiaPorAlimento(Alimento alimento) {
        return alimento.energiaBase;
    }

    // Felicidad que da cada juego a esta especie (por defecto la del juego).
    protected int felicidadPorJuego(Jugar juego) {
        return juego.aumentoFelicidad;
    }

    // Energía que gasta cada juego a esta especie (por defecto la del juego).
    protected int energiaPorJuego(Jugar juego) {
        return juego.gastoEnergetico;
    }

    // Mensaje de cómo juega esta especie (por defecto uno genérico).
    protected String describirJuego(Jugar juego) {
        return nombre + " jugo a " + juego.nombre + ".";
    }

    // Se ejecuta cada hora que pasa. Por defecto no hace nada.
    protected void alPasarTiempo() {
    }

    // Se ejecuta una sola vez cuando la mascota muere. Por defecto no hace nada.
    protected void alMorir() {
    }

    // Marca a la mascota como muerta y la despierta.
    private void morir() {
        muerto = true;
        durmiendo = false;
        alMorir();
    }

    // Mensaje para cuando se intenta usar una mascota muerta.
    protected String mensajeMuerto() {
        return nombre + " ha muerto y ya no puede hacer nada.";
    }

    // Agrega el aviso de muerte al mensaje si la acción mató a la mascota.
    private String conAvisoMuerte(String mensaje) {
        return muerto ? mensaje + " " + nombre + " ha muerto por falta de cuidados." : mensaje;
    }

    // Despierta a la mascota si dormía. Devuelve el aviso o texto vacío.
    private String despertarSiDuerme() {
        if (durmiendo) {
            durmiendo = false;
            return nombre + " se desperto. ";
        }
        return "";
    }

    // Baja la salud si hay mucha hambre o poca energía (pérdidas de 10 y 5).
    private void actualizarSalud() {
        descuidar(10, 5);
    }

    // Resta salud por descuido. Hambre y energía se revisan por separado y las pérdidas se suman.
    // Fuerte: hambre > 85 o energía < 15. Leve: hambre > 65 o energía < 35.
    private void descuidar(int perdidaFuerte, int perdidaLeve) {
        int perdida = 0;
        if (hambre > 85) {
            perdida += perdidaFuerte;
        } else if (hambre > 65) {
            perdida += perdidaLeve;
        }
        if (energia < 15) {
            perdida += perdidaFuerte;
        } else if (energia < 35) {
            perdida += perdidaLeve;
        }
        setSalud(salud - perdida);
    }

    // Dibujo corto de la especie, por ejemplo [Gato]. Cada especie lo define.
    public abstract String obtenDraw();

    // Tarjeta de estado: dibujo, nombre, ánimo, si duerme y los indicadores. Las subclases le agregan sus detalles.
    @Override
    public String toString() {
        return obtenDraw() + " " + nombre + " (" + getClass().getSimpleName() + ")\n"
                + "Animo: " + obtenerEstadoAnimo().getDescripcion() + "\n"
                + "Durmiendo: " + (durmiendo ? "Si" : "No") + "\n"
                + "Hambre: " + hambre + "/100 | Energia: " + energia
                + "/100 | Felicidad: " + felicidad + "/100 | Salud: " + salud + "/100";
    }
}
