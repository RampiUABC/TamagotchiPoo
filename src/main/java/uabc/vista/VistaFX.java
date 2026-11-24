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
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import uabc.Alimento;
import uabc.Jugar;
import uabc.controlador.Controlador;
import uabc.modelo.Tamagotchi;

import java.util.List;

// Ventana principal (JavaFX puro, sin CSS).
// Muestra las mascotas como tarjetas con emoji y le avisa al controlador cada clic.
public class VistaFX {

    // Tamaño de las tarjetas en la lista y en el panel derecho.
    private static final double TAM_LISTA = 92;
    private static final double TAM_DETALLE = 112;

    private final Stage ventana;
    private final Scene escena;
    private Controlador controlador;

    // Controles de la barra superior.
    private ComboBox<String> cmbEspecie;
    private TextField txtNombre;

    // Controles del centro y panel derecho.
    private FlowPane zonaMascotas;
    private StackPane contenedorCuadro;
    private Label lblEstado;
    private VBox panelAcciones;
    private ComboBox<String> cmbAlimento;
    private ComboBox<String> cmbJuego;
    private Button btnEspecial1;
    private Button btnEspecial2;

    // Texto de estado en la barra inferior.
    private Label lblMensaje;

    public VistaFX(Stage ventana) {
        this.ventana = ventana;
        this.escena = new Scene(construirInterfaz(), 920, 580);
    }

    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    public void mostrarVentana() {
        ventana.setTitle("Simulador Tamagotchi");
        ventana.setScene(escena);
        ventana.show();
    }

    // Redibuja las tarjetas de las mascotas y actualiza el panel derecho.
    public void actualizar(List<Tamagotchi> mascotas, Tamagotchi seleccionada) {
        zonaMascotas.getChildren().clear();
        for (Tamagotchi t : mascotas) {
            VBox tarjeta = crearTarjeta(t, TAM_LISTA, t == seleccionada, false);
            tarjeta.setCursor(Cursor.HAND);
            tarjeta.setOnMouseClicked(e -> controlador.seleccionarMascota(t));
            zonaMascotas.getChildren().add(tarjeta);
        }
        actualizarDetalle(seleccionada);
    }

    public void mostrarMensaje(String mensaje) {
        lblMensaje.setText(mensaje);
    }

    // Divide la ventana en 4 zonas: arriba, centro, derecha y abajo.
    private BorderPane construirInterfaz() {
        BorderPane raiz = new BorderPane();
        raiz.setBackground(fondo("#f8fafc", 0));
        raiz.setTop(crearBarraAdopcion());
        raiz.setCenter(crearZonaMascotas());
        raiz.setRight(crearPanelDetalle());
        raiz.setBottom(crearBarraMensaje());
        return raiz;
    }

    // Barra superior: elegir especie, escribir nombre y botón Adoptar.
    private HBox crearBarraAdopcion() {
        Label lblTitulo = new Label("Nueva mascota:");
        lblTitulo.setFont(Font.font("System", FontWeight.BOLD, 12.5));
        lblTitulo.setTextFill(Color.web("#334155"));

        cmbEspecie = new ComboBox<>();
        cmbEspecie.getItems().addAll("Perro", "Gato", "Muñeca", "Cuyo", "Ditto", "Sigilyph");
        cmbEspecie.getSelectionModel().selectFirst();
        cmbEspecie.setPrefWidth(125);
        cmbEspecie.setBackground(fondo("#f8fafc", 6));
        cmbEspecie.setBorder(borde("#cbd5e1", 1, 6));

        txtNombre = new TextField();
        txtNombre.setPromptText("Nombre");
        txtNombre.setBackground(fondo("#f8fafc", 6));
        txtNombre.setBorder(borde("#cbd5e1", 1, 6));
        txtNombre.setPadding(new Insets(6, 8, 6, 8));
        txtNombre.setOnAction(e -> alPulsarAdoptar());
        HBox.setHgrow(txtNombre, Priority.ALWAYS);

        Button btnAdoptar = new Button("Adoptar");
        pintarBoton(btnAdoptar, "#00723f", "#00723f", "#ffffff");
        btnAdoptar.setOnAction(e -> alPulsarAdoptar());

        HBox barra = new HBox(10, lblTitulo, cmbEspecie, txtNombre, btnAdoptar);
        barra.setPadding(new Insets(10, 14, 10, 14));
        barra.setAlignment(Pos.CENTER_LEFT);
        barra.setBackground(fondo("#ffffff", 0));
        barra.setBorder(bordeLado("#e2e8f0", 0, 0, 1, 0));
        return barra;
    }

    // Zona central con scroll donde aparecen todas las mascotas adoptadas.
    private ScrollPane crearZonaMascotas() {
        zonaMascotas = new FlowPane(14, 14);
        zonaMascotas.setPadding(new Insets(18));
        zonaMascotas.setBackground(fondo("#fafafa", 0));

        ScrollPane scroll = new ScrollPane(zonaMascotas);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setBorder(Border.EMPTY);
        return scroll;
    }

    // Panel derecho: tarjeta grande, cuadro de estadísticas y botones de acción.
    private VBox crearPanelDetalle() {
        contenedorCuadro = new StackPane();
        contenedorCuadro.setMinHeight(TAM_DETALLE);

        lblEstado = new Label();
        lblEstado.setWrapText(true);
        lblEstado.setMaxWidth(Double.MAX_VALUE);
        lblEstado.setFont(Font.font("System", 11.5));
        lblEstado.setTextFill(Color.web("#1e293b"));
        lblEstado.setPadding(new Insets(10));
        lblEstado.setBackground(fondo("#f8fafc", 8));
        lblEstado.setBorder(borde("#e2e8f0", 1, 8));

        VBox panel = new VBox(12, contenedorCuadro, lblEstado, crearPanelAcciones());
        panel.setPadding(new Insets(16));
        panel.setPrefWidth(350);
        panel.setAlignment(Pos.TOP_CENTER);
        panel.setBackground(fondo("#ffffff", 0));
        panel.setBorder(bordeLado("#e2e8f0", 0, 0, 0, 1));
        return panel;
    }

    // Botones y listas para alimentar, jugar, dormir y acciones especiales.
    private VBox crearPanelAcciones() {
        cmbAlimento = new ComboBox<>();
        for (Alimento a : Alimento.values()) {
            cmbAlimento.getItems().add(a.nombre);
        }
        cmbAlimento.getSelectionModel().selectFirst();
        cmbAlimento.setMaxWidth(Double.MAX_VALUE);
        cmbAlimento.setBackground(fondo("#f8fafc", 6));
        cmbAlimento.setBorder(borde("#cbd5e1", 1, 6));
        HBox.setHgrow(cmbAlimento, Priority.ALWAYS);

        Button btnAlimentar = new Button("Alimentar");
        btnAlimentar.setMinWidth(92);
        pintarBoton(btnAlimentar, "#e2e8f0", "#cbd5e1", "#0f172a");
        btnAlimentar.setOnAction(e ->
                controlador.alimentar(Alimento.values()[cmbAlimento.getSelectionModel().getSelectedIndex()]));

        cmbJuego = new ComboBox<>();
        for (Jugar j : Jugar.values()) {
            cmbJuego.getItems().add(j.nombre);
        }
        cmbJuego.getSelectionModel().selectFirst();
        cmbJuego.setMaxWidth(Double.MAX_VALUE);
        cmbJuego.setBackground(fondo("#f8fafc", 6));
        cmbJuego.setBorder(borde("#cbd5e1", 1, 6));
        HBox.setHgrow(cmbJuego, Priority.ALWAYS);

        Button btnJugar = new Button("Jugar");
        btnJugar.setMinWidth(92);
        pintarBoton(btnJugar, "#e2e8f0", "#cbd5e1", "#0f172a");
        btnJugar.setOnAction(e ->
                controlador.jugar(Jugar.values()[cmbJuego.getSelectionModel().getSelectedIndex()]));

        Button btnDormir = new Button("Dormir");
        btnDormir.setMaxWidth(Double.MAX_VALUE);
        pintarBoton(btnDormir, "#e2e8f0", "#cbd5e1", "#0f172a");
        btnDormir.setOnAction(e -> controlador.dormir());

        btnEspecial1 = new Button("Acción 1");
        btnEspecial1.setMaxWidth(Double.MAX_VALUE);
        pintarBoton(btnEspecial1, "#fef3c7", "#fcd34d", "#0f172a");
        btnEspecial1.setOnAction(e -> controlador.ejecutarAccionEspecial(0));

        btnEspecial2 = new Button("Acción 2");
        btnEspecial2.setMaxWidth(Double.MAX_VALUE);
        pintarBoton(btnEspecial2, "#fef3c7", "#fcd34d", "#0f172a");
        btnEspecial2.setOnAction(e -> controlador.ejecutarAccionEspecial(1));

        panelAcciones = new VBox(7,
                new HBox(7, cmbAlimento, btnAlimentar),
                new HBox(7, cmbJuego, btnJugar),
                btnDormir,
                btnEspecial1,
                btnEspecial2);
        return panelAcciones;
    }

    // Barra inferior con botón de tiempo y mensaje de lo último que ocurrió.
    private HBox crearBarraMensaje() {
        Button btnTiempo = new Button("Avanzar 1 hora");
        pintarBoton(btnTiempo, "#e2e8f0", "#cbd5e1", "#0f172a");
        btnTiempo.setOnAction(e -> controlador.pasarTiempo());

        Label lblPrefijo = new Label("Estado:");
        lblPrefijo.setFont(Font.font("System", FontWeight.BOLD, 12));
        lblPrefijo.setTextFill(Color.web("#0f172a"));

        lblMensaje = new Label();
        lblMensaje.setFont(Font.font("System", 12));
        lblMensaje.setTextFill(Color.web("#334155"));

        HBox barra = new HBox(10, btnTiempo, lblPrefijo, lblMensaje);
        barra.setAlignment(Pos.CENTER_LEFT);
        barra.setPadding(new Insets(9, 14, 9, 14));
        barra.setBackground(fondo("#f8fafc", 0));
        barra.setBorder(bordeLado("#e2e8f0", 1, 0, 0, 0));
        return barra;
    }

    private void alPulsarAdoptar() {
        int especie = cmbEspecie.getSelectionModel().getSelectedIndex() + 1;
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            nombre = "Sin nombre";
        }
        txtNombre.clear();
        controlador.adoptarTamagotchi(especie, nombre);
    }

    // Actualiza la tarjeta grande, el texto de estado y los nombres de botones especiales.
    private void actualizarDetalle(Tamagotchi seleccionada) {
        if (seleccionada == null) {
            contenedorCuadro.getChildren().clear();
            lblEstado.setText("Adopta o selecciona una mascota.");
            panelAcciones.setDisable(true);
            return;
        }
        contenedorCuadro.getChildren().setAll(crearTarjeta(seleccionada, TAM_DETALLE, false, true));
        lblEstado.setText(seleccionada.toString());

        String[] especiales = seleccionada.getNombresAccionesEspeciales();
        btnEspecial1.setText(especiales[0]);
        btnEspecial2.setText(especiales[1]);
        panelAcciones.setDisable(false);
    }

    // Crea una tarjeta redondeada con el color de la especie, su emoji y su nombre.
    private VBox crearTarjeta(Tamagotchi t, double tam, boolean seleccionada, boolean grande) {
        double radio = grande ? 10 : 8;

        Label emoji = new Label(emojiDe(t));
        emoji.setFont(Font.font(grande ? 34 : 24));

        Label nombre = new Label(t.getNombre());
        nombre.setFont(Font.font("System", FontWeight.BOLD, grande ? 14 : 12.5));
        nombre.setTextFill(Color.web("#0f172a"));
        nombre.setMaxWidth(tam - 12);
        nombre.setAlignment(Pos.CENTER);

        VBox tarjeta = new VBox(4, emoji, nombre);
        tarjeta.setAlignment(Pos.CENTER);
        tarjeta.setPrefSize(tam, tam);
        tarjeta.setMinSize(tam, tam);
        tarjeta.setMaxSize(tam, tam);
        tarjeta.setBackground(fondo(colorDe(t), radio));

        if (seleccionada && !grande) {
            tarjeta.setBorder(borde("#0f172a", 3, radio));
            tarjeta.setEffect(new DropShadow(12, 0, 4, Color.rgb(15, 23, 42, 0.22)));
        } else {
            tarjeta.setBorder(borde("#94a3b8", grande ? 2 : 1.5, radio));
        }
        return tarjeta;
    }

    // Emoji correspondiente a cada especie.
    private String emojiDe(Tamagotchi t) {
        return switch (t.getClass().getSimpleName()) {
            case "Perro" -> "🐶";
            case "Gato" -> "🐱";
            case "Muneca" -> "🎀";
            case "Cuyo" -> "🐹";
            case "Ditto" -> "🟣";
            case "Sigilyph" -> "🪶";
            default -> "🐾";
        };
    }

    // Color de fondo correspondiente a cada especie.
    private String colorDe(Tamagotchi t) {
        return switch (t.getClass().getSimpleName()) {
            case "Perro" -> "#c68b59";
            case "Gato" -> "#f4a259";
            case "Muneca" -> "#f7a8c4";
            case "Cuyo" -> "#d6b98c";
            case "Ditto" -> "#d8b4fe";
            case "Sigilyph" -> "#f5d547";
            default -> "#e2e8f0";
        };
    }

    // ===== MÉTODOS CORTOS PARA APLICAR COLORES Y BORDES EN JAVAFX =====

    private void pintarBoton(Button boton, String colorFondo, String colorBorde, String colorTexto) {
        boton.setBackground(fondo(colorFondo, 6));
        boton.setBorder(borde(colorBorde, 1, 6));
        boton.setTextFill(Color.web(colorTexto));
        boton.setFont(Font.font("System", FontWeight.BOLD, 12));
        boton.setPadding(new Insets(6, 12, 6, 12));
        boton.setCursor(Cursor.HAND);
    }

    private Background fondo(String colorHex, double radio) {
        return new Background(new BackgroundFill(Color.web(colorHex), new CornerRadii(radio), Insets.EMPTY));
    }

    private Border borde(String colorHex, double grosor, double radio) {
        return new Border(new BorderStroke(
                Color.web(colorHex), BorderStrokeStyle.SOLID, new CornerRadii(radio), new BorderWidths(grosor)
        ));
    }

    private Border bordeLado(String colorHex, double arriba, double derecha, double abajo, double izquierda) {
        return new Border(new BorderStroke(
                Color.web(colorHex), BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                new BorderWidths(arriba, derecha, abajo, izquierda)
        ));
    }
}