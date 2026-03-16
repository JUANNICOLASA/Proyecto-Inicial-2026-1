/**
 * Representa una taza. Hereda de TowerItem.
 * @authors Juan Nicolás Álvarez, Leonardo Rojas
 */
public class Cup extends TowerItem {
    private Rectangle outer;
    private Rectangle inner; 
    private boolean isCovered;
    
    public static final int CUP_HEIGHT = 20;
    private static final int WIDTH_FACTOR = 10;
    private static final int BASE_WIDTH = 20;

    public Cup(int size, String color) {
        super(size, color);
        this.isCovered = false;
    
        outer = new Rectangle();
        outer.changeColor(color);
        
        inner = new Rectangle();
        inner.changeColor("white"); 
        
        int width = BASE_WIDTH + (size * WIDTH_FACTOR);
        outer.changeSize(CUP_HEIGHT, width);
        
        int innerWidth = width - 6; 
        if (innerWidth < 1) innerWidth = 1;
        
        inner.changeSize(CUP_HEIGHT - 3, innerWidth);
        
        inner.moveHorizontal(3); 
    }

    @Override
    public void setPosition(int targetX, int targetY) {
        int width = BASE_WIDTH + (size * WIDTH_FACTOR);
        int finalX = targetX - (width / 2);
        
        int deltaX = finalX - this.currX;
        int deltaY = targetY - this.currY;
        
        outer.moveHorizontal(deltaX);
        outer.moveVertical(deltaY);
        inner.moveHorizontal(deltaX);
        inner.moveVertical(deltaY);
        
        this.currX = finalX;
        this.currY = targetY;
        
        outer.makeVisible();
        inner.makeVisible();
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
    }
    
    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            outer.makeVisible();
            inner.makeVisible();
        } else {
            outer.makeInvisible();
            inner.makeInvisible();
        }
    }

    @Override
    public int getHeight() { return CUP_HEIGHT; }

    @Override
    public String getTypeString() { return "cup"; }

    @Override
    public int getAddedHeightWhenPlacedOn(TowerItem itemBelow) {
        if (itemBelow == null) return CUP_HEIGHT; 
        
        if (itemBelow.getTypeString().equals("cup") && itemBelow.getSize() > this.size) {
            return 0; 
        }
        return CUP_HEIGHT; 
    }

    @Override
    public boolean canCover(TowerItem itemBelow) {
        return false;
    }
}