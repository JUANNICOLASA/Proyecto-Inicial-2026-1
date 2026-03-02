/**
 * Representa una tapa en el simulador de Stacking Cups.
 * Esta clase maneja la representación visual y el posicionamiento de una tapa
 * utilizando la clase Rectangle del paquete shapes.
 * * @author Juan Nicolás Álvarez, Leonardo Rojas
 * @version 2.0 (Ciclo 2)
 */
public class Lid {
    private int size;
    private Rectangle visual;
    private String color;
    private int currX;
    private int currY;
    
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
        int width = BASE_WIDTH + (size * WIDTH_FACTOR);
        this.visual.changeSize(LID_HEIGHT, width);
        this.currX = 70;
        this.currY = 15;
    }

    /**
     * Mueve la tapa a una posición absoluta (x, y) en el Canvas.
     * Calcula internamente el desplazamiento relativo (Deltas) necesario.
     * * @param targetX La coordenada X central deseada.
     * @param targetY La coordenada Y superior deseada.
     */
    public void setPosition(int targetX, int targetY) {
        int width = BASE_WIDTH + (size * WIDTH_FACTOR);
        int finalX = targetX - (width / 2);
        int deltaX = finalX - this.currX;
        int deltaY = targetY - this.currY;
        
        this.visual.moveHorizontal(deltaX);
        this.visual.moveVertical(deltaY);
        this.currX = finalX;
        this.currY = targetY;
        this.visual.makeVisible();
    }
    
    /**
     * Obtiene el objeto Rectangle visual asociado a esta tapa.
     * * @return El objeto Rectangle visual.
     */
    public Rectangle getVisual() { 
        return visual; 
    }
    
    /**
     * Obtiene el tamaño numérico de la tapa.
     * * @return El tamaño entero de la tapa.
     */
    public int getSize() { 
        return size; 
    }
    
    /**
     * Obtiene la altura visual de la tapa.
     * * @return La altura en píxeles.
     */
    public int getHeight() { 
        return LID_HEIGHT; 
    }
}