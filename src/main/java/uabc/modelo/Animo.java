package uabc.modelo;

// Estados de ánimo de la mascota, de mejor a peor.
public enum Animo {

    // Todo va muy bien: salud alta, buena energía y poca hambre.
    EXCELENTE("Se siente excelente y lleno de energía"),

    // Estado intermedio, nada urgente.
    NORMAL("Se siente bien y tranquilo"),

    // Algo está bajo y hay que atenderla pronto.
    REQUIERE_ATENCION("Necesita atención pronto"),

    // Algo está en nivel peligroso, necesita cuidados ya.
    CRITICO("¡Está mal! Necesita cuidados urgentes"),

    // La salud llegó a 0 y ya no puede hacer nada.
    MUERTO("Ha muerto por falta de cuidados");

    // Texto que acompaña a cada estado.
    private final String descripcion;

    // Guarda la descripción de cada estado.
    Animo(String descripcion) {
        this.descripcion = descripcion;
    }

    // Devuelve la descripción del estado.
    public String getDescripcion() {
        return descripcion;
    }
}
