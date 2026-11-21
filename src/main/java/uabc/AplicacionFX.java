package uabc;
import javafx.application.Application;
import javafx.stage.Stage;
import uabc.controlador.Controlador;
import uabc.vista.VistaFX;

// Aplicación JavaFX. Arma la vista, el modelo y el controlador (MVC) y arranca el programa.
public class AplicacionFX extends Application {

    // Se ejecuta al abrir la app: crea la vista, la colección vacía y el controlador.
    @Override
    public void start(Stage ventana) {
        VistaFX vista = new VistaFX(ventana);
        uabc.modelo.Coleccion coleccion = new uabc.modelo.Coleccion();
        Controlador controlador = new Controlador(vista, coleccion);
        controlador.iniciar();
    }
}
