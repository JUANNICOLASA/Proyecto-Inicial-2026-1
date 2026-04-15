package tower;

/**
 * Clase abstracta que define el contrato para cualquier pieza (ítem) que pertenezca a la torre.
 * @authors Juan Nicolás Álvarez, Leonardo Rojas
 */
public abstract class TowerItem {
    protected int size;
    protected String originalColor;
    protected int currX;
    protected int currY;

    /**
     * Constructor base para un ítem de la torre.
     * @param size El tamaño lógico del ítem.
     * @param color El color inicial del ítem.
     */
    public TowerItem(int size, String color) {
        this.size = size;
        this.originalColor = color;
        this.currX = 70;
        this.currY = 15;
    }

    /**
     * Obtiene el tamaño del ítem.
     * @return El tamaño lógico como entero.
     */
    public int getSize() { return size; }

    /**
     * Obtiene el color original del ítem.
     * @return El color como cadena de texto.
     */
    public String getColor() { return originalColor; }

    /**
     * Establece la posición gráfica del ítem en el lienzo.
     * @param x Coordenada X central.
     * @param y Coordenada Y de la base.
     */
    public abstract void setPosition(int x, int y);

    /**
     * Controla la visibilidad del ítem.
     * @param visible true para mostrar, false para ocultar.
     */
    public abstract void setVisible(boolean visible);

    /**
     * Obtiene la altura física del ítem en el lienzo.
     * @return Altura en píxeles.
     */
    public abstract int getHeight();
    
    /**
     * Obtiene el tipo de ítem en formato texto ("cup" o "lid").
     * @return El nombre genérico del tipo de ítem para comprobaciones.
     */
    public abstract String getTypeString();

    /**
     * Calcula cuánta altura adicional añade este ítem cuando se coloca sobre otro.
     * @param itemBelow El ítem sobre el cual se va a posicionar (puede ser null).
     * @return La altura en píxeles que añade a la pila.
     */
    public abstract int getAddedHeightWhenPlacedOn(TowerItem itemBelow);

    /**
     * Evalúa si este ítem tiene la capacidad de tapar perfectamente al ítem de abajo.
     * @param itemBelow El ítem que se pretende tapar.
     * @return true si lo puede tapar, false en caso contrario.
     */
    public abstract boolean canCover(TowerItem itemBelow);

    /**
     * Establece el estado visual de este ítem cuando ha sido tapado o destapado por otro.
     * @param covered true si está siendo tapado, false en caso contrario.
     */
    public abstract void setCovered(boolean covered);
}