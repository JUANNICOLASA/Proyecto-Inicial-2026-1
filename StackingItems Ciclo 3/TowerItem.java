/**
 * Clase abstracta que define el contrato para cualquier pieza de la torre.
 * @authors Juan Nicolás Álvarez, Leonardo Rojas
 */
public abstract class TowerItem {
    protected int size;
    protected String originalColor;
    protected int currX;
    protected int currY;

    public TowerItem(int size, String color) {
        this.size = size;
        this.originalColor = color;
        this.currX = 70;
        this.currY = 15;
    }

    public int getSize() { return size; }
    public String getColor() { return originalColor; }

    public abstract void setPosition(int x, int y);
    public abstract void setVisible(boolean visible);
    public abstract int getHeight();
    
    public abstract String getTypeString();

    /**
     * Define cuánta altura aporta este objeto al caer sobre otro.
     */
    public abstract int getAddedHeightWhenPlacedOn(TowerItem itemBelow);
    
    /**
     * Define si este objeto tiene la capacidad lógica de tapar al de abajo.
     */
    public abstract boolean canCover(TowerItem itemBelow);
    
    /**
     * Define cómo reacciona visualmente el objeto si alguien lo tapa.
     */
    public abstract void setCovered(boolean covered);
}