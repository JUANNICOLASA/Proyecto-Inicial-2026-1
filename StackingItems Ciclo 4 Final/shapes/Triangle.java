package shapes;

import java.awt.Polygon;

/**
 * A triangle that can be manipulated and that draws itself on a canvas.
 */
public class Triangle extends Shape {
    public static int VERTICES = 3;
    private int height;
    private int width;

    public Triangle() {
        height = 30;
        width = 40;
        xPosition = 140;
        yPosition = 15;
        color = "green";
        isVisible = false;
    }

    public void changeSize(int newHeight, int newWidth) {
        erase();
        height = newHeight;
        width = newWidth;
        draw();
    }

    @Override
    protected void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            int[] xpoints = { xPosition, xPosition + (width / 2), xPosition - (width / 2) };
            int[] ypoints = { yPosition, yPosition + height, yPosition + height };
            canvas.draw(this, color, new Polygon(xpoints, ypoints, 3));
            canvas.wait(10);
        }
    }

    @Override
    protected void erase() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
}