import java.awt.Color;

/**
 * Representa una taza en el simulador de Stacking Cups.
 * Utiliza composición sobre la clase Rectangle del paquete shapes para dibujarse.
 * * @authors Juan Nicolás Álvarez, Leonardo Rojas
 * @version 1.0 (Ciclo 1)
 */
public class Cup {
    private int size;
    private Rectangle visual;
    private String color;
    
    // Variables para rastrear la posición actual
    private int currX;
    private int currY;
    
    // Constantes de dimensión visual
    /** Altura fija de la taza en píxeles */
    public static final int CUP_HEIGHT = 20;
    private static final int WIDTH_FACTOR = 10;
    private static final int BASE_WIDTH = 20;

    /**
     * Crea una nueva taza con un tamaño y color específicos.
     * * @param size El tamaño numérico de la taza (1, 2, 3...)
     * @param color El color de la taza (ej. "red", "blue")
     */
    public Cup(int size, String color) {
        this.size = size;
        this.color = color;
        this.visual = new Rectangle();
        this.visual.changeColor(color);
        
        // Calcular ancho visual
        int width = BASE_WIDTH + (size * WIDTH_FACTOR);
        this.visual.changeSize(CUP_HEIGHT, width);
        
        // Inicializamos con la posición por defecto
        this.currX = 70;
        this.currY = 15;
    }

    /**
     * Mueve la taza a una posición absoluta (x, y) en el Canvas.
     * Calcula internamente el desplazamiento relativo necesario.
     * * @param targetX La coordenada X central deseada.
     * @param targetY La coordenada Y superior deseada.
     */
    public void setPosition(int targetX, int targetY) {
        int width = BASE_WIDTH + (size * WIDTH_FACTOR);
        
        // 1. Calcular coordenada X de la esquina izquierda
        int finalX = targetX - (width / 2);
        
        // 2. Calcular delta
        int deltaX = finalX - this.currX;
        int deltaY = targetY - this.currY;
        
        // 3. Mover visualmente
        this.visual.moveHorizontal(deltaX);
        this.visual.moveVertical(deltaY);
        
        // 4. Actualizar estado interno
        this.currX = finalX;
        this.currY = targetY;
        
        this.visual.makeVisible();
    }
    
    /**
     * Cambia la visibilidad de la taza.
     * @param visible true para mostrar, false para ocultar.
     */
    public void setVisible(boolean visible) {
        if (visible) visual.makeVisible();
        else visual.makeInvisible();
    }

    /**
     * Obtiene el tamaño numérico de la taza.
     * @return El tamaño.
     */
    public int getSize() { return size; }
    
    /**
     * Obtiene el color de la taza.
     * @return El nombre del color.
     */
    public String getColor() { return color; }
    
    /**
     * Obtiene la altura visual de la taza.
     * @return La altura en píxeles.
     */
    public int getHeight() { return CUP_HEIGHT; }
}