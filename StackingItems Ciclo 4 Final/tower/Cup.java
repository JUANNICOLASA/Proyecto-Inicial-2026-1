package tower;

import shapes.Rectangle;

/**
 * Representa una taza estándar del simulador. 
 * Hereda de TowerItem y puede contener a otras tazas o ser tapada por una tapa compatible.
 * @authors Juan Nicolás Álvarez, Leonardo Rojas
 */
public class Cup extends TowerItem {
    private Rectangle outer;
    private Rectangle inner; 
    private Rectangle sealedIndicator; 
    private boolean isCovered;
    private boolean isFullySealed; 
    
    private int height; 
    private int width;
    private static final int WIDTH_FACTOR = 10;
    private static final int BASE_WIDTH = 20;

    /**
     * Construye una taza con el tamaño y color especificados.
     * @param size El tamaño de la taza.
     * @param color El color visual de la taza.
     */
    public Cup(int size, String color) {
        super(size, color);
        this.isCovered = false;
        this.isFullySealed = false;
        this.width = BASE_WIDTH + (size * WIDTH_FACTOR);
        this.height = 15 + (size * 5); 
    
        outer = new Rectangle();
        outer.changeColor(color);
        outer.changeSize(this.height, this.width);
        
        inner = new Rectangle();
        inner.changeColor("white"); 
        
        int innerWidth = this.width - 6; 
        if (innerWidth < 1) innerWidth = 1;
        
        inner.changeSize(this.height - 3, innerWidth);
        inner.moveHorizontal(3); 
        
        sealedIndicator = new Rectangle();
        sealedIndicator.changeColor(color); 
        sealedIndicator.changeSize(8, this.width + 4); 
        sealedIndicator.moveVertical((this.height / 2) - 4); 
        sealedIndicator.moveHorizontal(-2); 
    }

    @Override
    public void setPosition(int targetX, int targetY) {
        int finalX = targetX - (this.width / 2);
        
        int deltaX = finalX - this.currX;
        int deltaY = targetY - this.currY;
        
        outer.moveHorizontal(deltaX);
        outer.moveVertical(deltaY);
        inner.moveHorizontal(deltaX);
        inner.moveVertical(deltaY);
        sealedIndicator.moveHorizontal(deltaX);
        sealedIndicator.moveVertical(deltaY);
        
        this.currX = finalX;
        this.currY = targetY;
        
        outer.makeVisible();
        inner.makeVisible();
        
        updateVisibility(); 
    }
    
    /**
     * Define si el contenido interior de la taza está completamente sellado visualmente.
     * @param sealed true si está sellado, false si no.
     */
    public void setFullySealed(boolean sealed) {
        this.isFullySealed = sealed;
        updateVisibility();
    }

    @Override
    public void setCovered(boolean covered) {
        this.isCovered = covered;
        
        if (covered) {
            outer.changeColor("black");
            inner.changeColor("black"); 
        } else {
            outer.changeColor(originalColor);
            inner.changeColor("white"); 
        }
        
        updateVisibility();
    }
    
    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            outer.makeVisible();
            inner.makeVisible();
            updateVisibility();
        } else {
            outer.makeInvisible();
            inner.makeInvisible();
            sealedIndicator.makeInvisible();
        }
    }
    
    private void updateVisibility() {
        if (isCovered && isFullySealed) {
            sealedIndicator.makeVisible();
        } else {
            sealedIndicator.makeInvisible();
        }
    }

    @Override
    public int getHeight() { return this.height; }

    @Override
    public String getTypeString() { 
        return "cup"; 
    }

    @Override
    public int getAddedHeightWhenPlacedOn(TowerItem itemBelow) {
        if (itemBelow == null) return this.height; 
        if (itemBelow instanceof Cup && itemBelow.getSize() > this.size) {
            return 0; 
        }
        return this.height; 
    }

    @Override
    public boolean canCover(TowerItem itemBelow) { return false; }
}