/**
 * Representa una taza en el simulador de Stacking Cups.
 * Utiliza composición sobre la clase Rectangle del paquete shapes para dibujarse.
 * Gestiona el requisito de usabilidad para cambiar de aspecto visual al ser tapada.
 * * @author Juan Nicolás Álvarez, Leonardo Rojas
 * @version 2.0 (Ciclo 2)
 */
public class Cup {
    private int size;
    private Rectangle visual;
    private String originalColor; 
    private boolean isCovered; 
    private int currX;
    private int currY;
    
    /** Altura fija de la taza en píxeles */
    public static final int CUP_HEIGHT = 20;
    private static final int WIDTH_FACTOR = 10;
    private static final int BASE_WIDTH = 20;

    /**
     * Construye una nueva taza con un tamaño numérico y un color base.
     * * @param size  Tamaño de la taza (usado para calcular su ancho).
     * @param color Nombre del color base en inglés.
     */
    public Cup(int size, String color) {
        this.size = size;
        this.originalColor = color;
        this.isCovered = false;
        
        this.visual = new Rectangle();
        this.visual.changeColor(color);
        
        int width = BASE_WIDTH + (size * WIDTH_FACTOR);
        this.visual.changeSize(CUP_HEIGHT, width);
        
        this.currX = 70;
        this.currY = 15;
    }

    /**
     * Mueve la taza a una coordenada absoluta calculando el desplazamiento.
     * * @param targetX Coordenada X central de destino.
     * @param targetY Coordenada Y superior de destino.
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
     * Cambia el aspecto visual de la taza cumpliendo el REQUISITO DE USABILIDAD.
     * * @param covered true si la taza está siendo tapada correctamente, false en caso contrario.
     */
    public void setCovered(boolean covered) {
        this.isCovered = covered;
        if (covered) {
            this.visual.changeColor("black"); 
        } else {
            this.visual.changeColor(originalColor);
        }
    }
    
    /**
     * Controla la visibilidad gráfica de la taza.
     * * @param visible true para hacerla visible, false para borrarla del canvas.
     */
    public void setVisible(boolean visible) {
        if (visible) visual.makeVisible();
        else visual.makeInvisible();
    }

    /**
     * Obtiene el tamaño lógico de la taza.
     * * @return Tamaño entero.
     */
    public int getSize() { return size; }

    /**
     * Obtiene el color original de la taza ignorando el estado de usabilidad.
     * * @return Nombre del color en texto.
     */
    public String getColor() { return originalColor; }

    /**
     * Retorna la altura estática de la taza.
     * * @return Altura en píxeles.
     */
    public int getHeight() { return CUP_HEIGHT; }
}