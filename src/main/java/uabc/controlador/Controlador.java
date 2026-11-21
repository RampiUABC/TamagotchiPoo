package uabc.controlador;

import uabc.Alimento;
import uabc.Jugar;
import uabc.modelo.*;
import uabc.vista.VistaFX;

// Conecta la vista con el modelo (MVC). Recibe lo que hace el usuario
// y se lo pide a la mascota seleccionada.
public class Controlador {

    // Ventana del programa.
    private VistaFX vista;

    // Colección con todas las mascotas.
    private Coleccion modelo;

    // Mascota elegida en este momento (null si no hay).
    private Tamagotchi seleccionado;

    // Guarda la vista y el modelo, y se los presenta a la vista.
    public Controlador(VistaFX vista, Coleccion modelo) {
        this.vista = vista;
        this.modelo = modelo;
        this.seleccionado = null;
        vista.setControlador(this);
    }

    // Muestra la ventana y el mensaje de bienvenida.
    public void iniciar() {
        vista.mostrarVentana();
        refrescarVista();
        vista.mostrarMensaje("Bienvenido. Adopta una mascota para empezar.");
    }

    // Crea una mascota según la especie (1 Perro, 2 Gato, 3 Muñeca, 4 Cuyo, 5 Ditto, 6 Sigilyph),
    // la agrega a la colección y la selecciona.
    public void adoptarTamagotchi(int especie, String nombre) {
        Tamagotchi nuevo = switch (especie) {
            case 1 -> new Perro(nombre);
            case 2 -> new Gato(nombre);
            case 3 -> new Muneca(nombre);
            case 4 -> new Cuyo(nombre);
            case 5 -> new Ditto(nombre);
            case 6 -> new Sigilyph(nombre);
            default -> throw new IllegalArgumentException("Especie invalida: " + especie);
        };
        modelo.agregar(nuevo);
        seleccionado = nuevo;
        refrescarVista();
        vista.mostrarMensaje("Has adoptado a " + nuevo.getNombre() + " (" + nuevo.getClass().getSimpleName() + ").");
    }

    // Selecciona la mascota que se eligió en pantalla.
    public void seleccionarMascota(Tamagotchi t) {
        seleccionado = t;
        refrescarVista();
        vista.mostrarMensaje("Seleccionaste a " + t.getNombre() + ".");
    }

    // Alimenta a la mascota seleccionada y muestra el resultado.
    public void alimentar(Alimento alimento) {
        if (!hayMascotaSeleccionada()) {
            return;
        }
        String mensaje = seleccionado.alimentar(alimento);
        refrescarVista();
        vista.mostrarMensaje(mensaje);
    }

    // Juega con la mascota seleccionada y muestra el resultado.
    public void jugar(Jugar juego) {
        if (!hayMascotaSeleccionada()) {
            return;
        }
        String mensaje = seleccionado.jugar(juego);
        refrescarVista();
        vista.mostrarMensaje(mensaje);
    }

    // Manda a dormir a la mascota seleccionada y muestra el resultado.
    public void dormir() {
        if (!hayMascotaSeleccionada()) {
            return;
        }
        String mensaje = seleccionado.dormir();
        refrescarVista();
        vista.mostrarMensaje(mensaje);
    }

    // Hace la acción especial (0 o 1) de la mascota seleccionada y muestra el resultado.
    public void ejecutarAccionEspecial(int indice) {
        if (!hayMascotaSeleccionada()) {
            return;
        }
        String mensaje = seleccionado.ejecutarAccionEspecial(indice);
        refrescarVista();
        vista.mostrarMensaje(mensaje);
    }

    // Pasa una hora para todas las mascotas y avisa si alguna murió.
    public void pasarTiempo() {
        if (modelo.estaVacia()) {
            vista.mostrarMensaje("Todavia no hay mascotas a las que avanzarles el tiempo.");
            return;
        }
        StringBuilder avisos = new StringBuilder();
        for (Tamagotchi t : modelo.getMisJuguetes()) {
            String aviso = t.pasarTiempo();
            if (!aviso.isEmpty()) {
                avisos.append(aviso).append(' ');
            }
        }
        refrescarVista();
        vista.mostrarMensaje(avisos.length() > 0
                ? "Paso 1 hora. " + avisos.toString().trim()
                : "Paso 1 hora.");
    }

    // Actualiza la pantalla con el estado actual.
    private void refrescarVista() {
        vista.actualizar(modelo.getMisJuguetes(), seleccionado);
    }

    // Revisa que haya una mascota seleccionada. Si no, avisa al usuario.
    private boolean hayMascotaSeleccionada() {
        if (seleccionado == null) {
            vista.mostrarMensaje("Primero selecciona una mascota.");
            return false;
        }
        return true;
    }
}
