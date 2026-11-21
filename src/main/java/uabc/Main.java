package uabc;
import javafx.application.Application;

// Punto de entrada del programa.
// No extiende Application a propósito, para evitar el error "JavaFX runtime components are missing".
public class Main {

    // Arranca la aplicación JavaFX.
    public static void main(String[] args) {
        Application.launch(AplicacionFX.class, args);
    }
}
