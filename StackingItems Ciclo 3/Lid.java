/**
 * Representa una tapa. Hereda de TowerItem.
 * @authors Juan Nicolás Álvarez, Leonardo Rojas
 */
public class Lid extends TowerItem {
    private Rectangle visual;
    public static final int LID_HEIGHT = 5;
    private static final int WIDTH_FACTOR = 10;
    private static final int BASE_WIDTH = 25; 

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
    public String getTypeString() { return "lid"; }

    @Override
    public int getAddedHeightWhenPlacedOn(TowerItem itemBelow) {
        if (canCover(itemBelow)) return 0; 
        return LID_HEIGHT; 
    }

    @Override
    public boolean canCover(TowerItem itemBelow) {
        return itemBelow != null && 
               itemBelow.getTypeString().equals("cup") && 
               itemBelow.getSize() == this.size;
    }

    @Override
    public void setCovered(boolean covered) {
    }
}