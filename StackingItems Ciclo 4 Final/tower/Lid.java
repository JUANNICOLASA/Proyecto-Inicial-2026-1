package tower;

import shapes.Rectangle;

/**
 * Representa una tapa estándar en el simulador. 
 * Hereda de TowerItem y tiene la capacidad de cubrir tazas del mismo tamaño.
 * @authors Juan Nicolás Álvarez, Leonardo Rojas
 */
public class Lid extends TowerItem {
    private Rectangle visual;
    public static final int LID_HEIGHT = 5;
    private static final int WIDTH_FACTOR = 10;
    private static final int BASE_WIDTH = 25; 

    /**
     * Construye una tapa con el tamaño y color especificados.
     * @param size El tamaño de la tapa.
     * @param color El color visual de la tapa.
     */
    public Lid(int size, String color) {
        super(size, color);
        this.visual = new Rectangle();
        this.visual.changeColor(color);
        
        int width = BASE_WIDTH + (size * WIDTH_FACTOR);
        this.visual.changeSize(LID_HEIGHT, width);
    }

    @Override
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
    
    @Override
    public void setVisible(boolean visible) {
        if (visible) visual.makeVisible();
        else visual.makeInvisible();
    }

    @Override
    public int getHeight() { return LID_HEIGHT; }

    @Override
    public String getTypeString() { 
        return "lid"; 
    }

    @Override
    public int getAddedHeightWhenPlacedOn(TowerItem itemBelow) {
        if (itemBelow == null) return LID_HEIGHT;
        if (canCover(itemBelow)) return 0; 
        if (itemBelow instanceof Cup && itemBelow.getSize() > this.size) {
            return 0; 
        }
        return LID_HEIGHT; 
    }

    @Override
    public boolean canCover(TowerItem itemBelow) {
        return itemBelow != null && 
               itemBelow instanceof Cup && 
               itemBelow.getSize() == this.size;
    }

    @Override
    public void setCovered(boolean covered) {
    }
}