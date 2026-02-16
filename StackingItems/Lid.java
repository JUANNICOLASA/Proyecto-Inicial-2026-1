import java.awt.Color;

/**
 * Representa una tapa en el simulador de Stacking Cups.
 * Esta clase maneja la representación visual y el posicionamiento de una tapa.
 * * @authors Juan Nicolás Álvarez, Leonardo Rojas
 * @version 1.0 (Ciclo 1)
 */
public class Lid {
    private int size;
    private Rectangle visual;
    private String color;
    
    // Variables para rastrear la posición actual 
    private int currX;
    private int currY;
    
    // Constantes de dimensión visual
    /** Altura fija de la tapa en píxeles (representa 1cm) */
    public static final int LID_HEIGHT = 5;
    private static final int WIDTH_FACTOR = 10;
    private static final int BASE_WIDTH = 25; 

    /**
     * Crea una nueva tapa con un tamaño y color específicos.
     * * @param size El tamaño numérico de la tapa (1, 2, 3...)
     * @param color El color de la tapa (ej. "red", "blue")
     */
    public Lid(int size, String color) {
        this.size = size;
        this.color = color;
        this.visual = new Rectangle();
        this.visual.changeColor(color);
        
        // Calcular ancho visual basado en el tamaño
        int width = BASE_WIDTH + (size * WIDTH_FACTOR);
        this.visual.changeSize(LID_HEIGHT, width);
        
        // Inicializamos con la posición por defecto de Rectangle (70, 15)
        this.currX = 70;
        this.currY = 15;
    }

    /**
     * Mueve la tapa a una posición absoluta (x, y) en el Canvas.
     * Calcula internamente el desplazamiento relativo necesario.
     * * @param targetX La coordenada X central deseada.
     * @param targetY La coordenada Y superior deseada.
     */
    public void setPosition(int targetX, int targetY) {
        int width = BASE_WIDTH + (size * WIDTH_FACTOR);
        
        // 1. Calcular coordenada X de la esquina izquierda (para centrar)
        int finalX = targetX - (width / 2);
        
        // 2. Calcular el delta (diferencia) desde la posición actual
        int deltaX = finalX - this.currX;
        int deltaY = targetY - this.currY;
        
        // 3. Mover el objeto visual
        this.visual.moveHorizontal(deltaX);
        this.visual.moveVertical(deltaY);
        
        // 4. Actualizar la posición actual conocida
        this.currX = finalX;
        this.currY = targetY;
        
        // Asegurar que sea visible
        this.visual.makeVisible();
    }
    
    /**
     * Obtiene el objeto Rectangle visual asociado a esta tapa.
     * @return El objeto Rectangle.
     */
    public Rectangle getVisual() { 
        return visual; 
    }
    
    /**
     * Obtiene el tamaño numérico de la tapa.
     * @return El tamaño.
     */
    public int getSize() { 
        return size; 
    }
    
    /**
     * Obtiene la altura visual de la tapa.
     * @return La altura en píxeles.
     */
    public int getHeight() { 
        return LID_HEIGHT; 
    }
}