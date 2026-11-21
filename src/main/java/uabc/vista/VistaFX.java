package uabc.vista;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import uabc.Alimento;
import uabc.Jugar;
import uabc.controlador.Controlador;
import uabc.modelo.Tamagotchi;

import java.util.List;

// Ventana principal (JavaFX). Muestra las mascotas como cuadros
// y le avisa al controlador lo que hace el usuario.
public class VistaFX {

    // Tamaño del cuadro en la lista de mascotas.
    private static final double TAM_CUADRO_LISTA = 90;

    // Tamaño del cuadro grande del panel de detalle.
    private static final double TAM_CUADRO_DETALLE = 140;

    // Ventana principal.
    private final Stage ventana;

    // Escena con toda la interfaz.
    private final Scene escena;

    // Controlador al que se le avisa de cada acción.
    private Controlador controlador;

    // Lista para elegir la especie a adoptar.
    private ComboBox<String> cmbEspecie;

    // Campo para escribir el nombre.
    private TextField txtNombre;

    // Zona donde se ven los cuadros de las mascotas.
    private FlowPane zonaMascotas;

    // Contenedor del cuadro grande de la mascota seleccionada.
    private StackPane contenedorCuadro;

    // Texto con la tarjeta de estado.
    private Label lblEstado;

    // Panel con los botones de acciones (se desactiva si no hay mascota).
    private VBox panelAcciones;

    // Lista de comidas.
    private ComboBox<String> cmbAlimento;

    // Lista de juegos.
    private ComboBox<String> cmbJuego;

    // Botón de la acción especial 1 (su texto cambia según la especie).
    private Button btnEspecial1;

    // Botón de la acción especial 2 (su texto cambia según la especie).
    private Button btnEspecial2;

    // Mensaje de lo que acaba de pasar.
    private Label lblMensaje;

    // Guarda la ventana y arma la interfaz.
    public VistaFX(Stage ventana) {
        this.ventana = ventana;
        this.escena = new Scene(construirInterfaz(), 900, 540);
    }

    // Guarda el controlador para avisarle de los clics.
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    // Muestra la ventana.
    public void mostrarVentana() {
        ventana.setTitle("Simulador Tamagotchi");
        ventana.setScene(escena);
        ventana.show();
    }

    // Redibuja los cuadros de las mascotas y el detalle de la seleccionada.
    public void actualizar(List<Tamagotchi> mascotas, Tamagotchi seleccionada) {
        zonaMascotas.getChildren().clear();
        for (Tamagotchi t : mascotas) {
            StackPane cuadro = crearCuadro(t, TAM_CUADRO_LISTA, t == seleccionada);
            cuadro.setCursor(Cursor.HAND);
            cuadro.setOnMouseClicked(e -> controlador.seleccionarMascota(t));
            zonaMascotas.getChildren().add(cuadro);
        }
        actualizarDetalle(seleccionada);
    }

    // Muestra un mensaje en la barra de abajo.
    public void mostrarMensaje(String mensaje) {
        lblMensaje.setText(mensaje);
    }

    // Arma la pantalla: adopción arriba, mascotas al centro, detalle a la derecha y mensajes abajo.
    private BorderPane construirInterfaz() {
        BorderPane raiz = new BorderPane();
        raiz.setTop(crearBarraAdopcion());
        raiz.setCenter(crearZonaMascotas());
        raiz.setRight(crearPanelDetalle());
        raiz.setBottom(crearBarraMensaje());
        return raiz;
    }

    // Barra de arriba para adoptar: especie, nombre y botón.
    private HBox crearBarraAdopcion() {
        cmbEspecie = new ComboBox<>();
        cmbEspecie.getItems().addAll("Perro", "Gato", "Muneca", "Cuyo", "Ditto", "Sigilyph");
        cmbEspecie.getSelectionModel().selectFirst();

        txtNombre = new TextField();
        txtNombre.setPromptText("Nombre");
        txtNombre.setOnAction(e -> alPulsarAdoptar());

        Button btnAdoptar = new Button("Adoptar");
        btnAdoptar.setOnAction(e -> alPulsarAdoptar());

        HBox barra = new HBox(10, new Label("Nueva mascota:"), cmbEspecie, txtNombre, btnAdoptar);
        barra.setPadding(new Insets(10));
        barra.setAlignment(Pos.CENTER_LEFT);
        return barra;
    }

    // Zona con scroll donde se ven las mascotas.
    private ScrollPane crearZonaMascotas() {
        zonaMascotas = new FlowPane(10, 10);
        zonaMascotas.setPadding(new Insets(10));

        ScrollPane scroll = new ScrollPane(zonaMascotas);
        scroll.setFitToWidth(true);
        return scroll;
    }

    // Panel derecho: cuadro grande, estado y acciones.
    private VBox crearPanelDetalle() {
        contenedorCuadro = new StackPane();
        contenedorCuadro.setMinHeight(TAM_CUADRO_DETALLE);

        lblEstado = new Label();
        lblEstado.setWrapText(true);

        VBox panel = new VBox(15, contenedorCuadro, lblEstado, crearPanelAcciones());
        panel.setPadding(new Insets(10));
        panel.setPrefWidth(340);
        panel.setAlignment(Pos.TOP_CENTER);
        panel.setStyle("-fx-border-color: lightgray; -fx-border-width: 0 0 0 1;");
        return panel;
    }

    // Crea los controles de alimentar, jugar, dormir y las dos acciones especiales.
    private VBox crearPanelAcciones() {
        cmbAlimento = new ComboBox<>();
        for (Alimento a : Alimento.values()) {
            cmbAlimento.getItems().add(a.nombre);
        }
        cmbAlimento.getSelectionModel().selectFirst();
        Button btnAlimentar = new Button("Alimentar");
        btnAlimentar.setOnAction(e ->
                controlador.alimentar(Alimento.values()[cmbAlimento.getSelectionModel().getSelectedIndex()]));

        cmbJuego = new ComboBox<>();
        for (Jugar j : Jugar.values()) {
            cmbJuego.getItems().add(j.nombre);
        }
        cmbJuego.getSelectionModel().selectFirst();
        Button btnJugar = new Button("Jugar");
        btnJugar.setOnAction(e ->
                controlador.jugar(Jugar.values()[cmbJuego.getSelectionModel().getSelectedIndex()]));

        Button btnDormir = new Button("Dormir");
        btnDormir.setMaxWidth(Double.MAX_VALUE);
        btnDormir.setOnAction(e -> controlador.dormir());

        btnEspecial1 = new Button("Accion 1");
        btnEspecial1.setMaxWidth(Double.MAX_VALUE);
        btnEspecial1.setOnAction(e -> controlador.ejecutarAccionEspecial(0));

        btnEspecial2 = new Button("Accion 2");
        btnEspecial2.setMaxWidth(Double.MAX_VALUE);
        btnEspecial2.setOnAction(e -> controlador.ejecutarAccionEspecial(1));

        panelAcciones = new VBox(8,
                new HBox(8, cmbAlimento, btnAlimentar),
                new HBox(8, cmbJuego, btnJugar),
                btnDormir, btnEspecial1, btnEspecial2);
        return panelAcciones;
    }

    // Barra de abajo con el botón de avanzar 1 hora y el mensaje.
    private HBox crearBarraMensaje() {
        lblMensaje = new Label();
        Button btnTiempo = new Button("Avanzar 1 hora");
        btnTiempo.setOnAction(e -> controlador.pasarTiempo());
        HBox barra = new HBox(12, btnTiempo, lblMensaje);
        barra.setAlignment(Pos.CENTER_LEFT);
        barra.setPadding(new Insets(8, 10, 8, 10));
        barra.setStyle("-fx-border-color: lightgray; -fx-border-width: 1 0 0 0;");
        return barra;
    }

    // Lee la especie y el nombre (si está vacío usa "Sin nombre") y pide adoptar.
    private void alPulsarAdoptar() {
        int especie = cmbEspecie.getSelectionModel().getSelectedIndex() + 1;
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            nombre = "Sin nombre";
        }
        txtNombre.clear();
        controlador.adoptarTamagotchi(especie, nombre);
    }

    // Muestra los datos de la mascota seleccionada, o desactiva las acciones si no hay.
    private void actualizarDetalle(Tamagotchi seleccionada) {
        if (seleccionada == null) {
            contenedorCuadro.getChildren().clear();
            lblEstado.setText("Adopta o selecciona una mascota.");
            panelAcciones.setDisable(true);
            return;
        }
        contenedorCuadro.getChildren().setAll(crearCuadro(seleccionada, TAM_CUADRO_DETALLE, false));
        lblEstado.setText(seleccionada.toString());

        String[] especiales = seleccionada.getNombresAccionesEspeciales();
        btnEspecial1.setText(especiales[0]);
        btnEspecial2.setText(especiales[1]);
        panelAcciones.setDisable(false);
    }

    // Crea un cuadrado de color con el nombre. Tiene borde grueso si está seleccionada.
    private StackPane crearCuadro(Tamagotchi t, double tam, boolean seleccionada) {
        Rectangle fondo = new Rectangle(tam, tam);
        fondo.setFill(colorDe(t));
        fondo.setStroke(seleccionada ? Color.BLACK : Color.GRAY);
        fondo.setStrokeWidth(seleccionada ? 4 : 1);

        Label nombre = new Label(t.getNombre());
        nombre.setFont(Font.font("System", FontWeight.BOLD, 13));
        nombre.setMaxWidth(tam - 8);

        return new StackPane(fondo, nombre);
    }

    // Color del cuadrado según la especie.
    private Color colorDe(Tamagotchi t) {
        return switch (t.getClass().getSimpleName()) {
            case "Perro" -> Color.web("#c68b59");
            case "Gato" -> Color.web("#f4a259");
            case "Muneca" -> Color.web("#f7a8c4");
            case "Cuyo" -> Color.web("#d8b98a");
            case "Ditto" -> Color.web("#c9a0dc");
            case "Sigilyph" -> Color.web("#8ecae6");
            default -> Color.LIGHTGRAY;
        };
    }
}
