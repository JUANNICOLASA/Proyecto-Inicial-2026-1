package shapes;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Canvas is a class to allow for simple graphical drawing on a canvas.
 * This is a modification of the general purpose Canvas, specially made for
 * the BlueJ "shapes" example. 
 *
 * @author: Bruce Quig
 * @author: Michael Kolling (mik)
 *
 * @version: 1.6 (shapes)
 */
public class Canvas {
    private static Canvas canvasSingleton;

    /**
     * Factory method to get the canvas singleton object.
     */
    public static Canvas getCanvas() {
        if (canvasSingleton == null) {
            canvasSingleton = new Canvas("BlueJ Shapes Demo", 500, 500, Color.white);
        }
        canvasSingleton.setVisible(true);
        return canvasSingleton;
    }

    private JFrame frame;
    private CanvasPane canvas;
    private Graphics2D graphic;
    private Color backgroundColour;
    private Image canvasImage;
    private List<Object> objects;
    private HashMap<Object, ShapeDescription> shapes;
    
    /**
     * Create a Canvas.
     */
    private Canvas(String title, int width, int height, Color bgColour) {
        frame = new JFrame();
        canvas = new CanvasPane();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        canvas.setPreferredSize(new Dimension(width, height));
        backgroundColour = bgColour;
        frame.pack();
        objects = new ArrayList<Object>();
        shapes = new HashMap<Object, ShapeDescription>();
    }

    public void setVisible(boolean visible) {
        if (graphic == null) {
            Dimension size = canvas.getSize();
            canvasImage = canvas.createImage(size.width, size.height);
            graphic = (Graphics2D) canvasImage.getGraphics();
            graphic.setColor(backgroundColour);
            graphic.fillRect(0, 0, size.width, size.height);
            graphic.setColor(Color.black);
        }
        frame.setVisible(visible);
    }

    /**
     * Draw a given shape onto the canvas.
     * NOTA: Usamos java.awt.Shape para evitar conflicto con nuestra clase abstracta Shape
     */
    public void draw(Object referenceObject, String color, java.awt.Shape shape) {
        objects.remove(referenceObject);   
        objects.add(referenceObject);      
        shapes.put(referenceObject, new ShapeDescription(shape, color));
        redraw();
    }
 
    public void erase(Object referenceObject) {
        objects.remove(referenceObject);   
        shapes.remove(referenceObject);
        redraw();
    }

    public void setForegroundColor(String colorString) {
        if (colorString.equals("red"))
            graphic.setColor(Color.red);
        else if (colorString.equals("black"))
            graphic.setColor(Color.black);
        else if (colorString.equals("blue"))
            graphic.setColor(Color.blue);
        else if (colorString.equals("yellow"))
            graphic.setColor(Color.yellow);
        else if (colorString.equals("green"))
            graphic.setColor(Color.green);
        else if (colorString.equals("magenta"))
            graphic.setColor(Color.magenta);
        else if (colorString.equals("white"))
            graphic.setColor(Color.white);
        else
            graphic.setColor(Color.black);
    }

    public void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) {
            // ignoring exception at the moment
        }
    }

    private void redraw() {
        erase();
        for (Iterator<Object> i = objects.iterator(); i.hasNext(); ) {
            shapes.get(i.next()).draw(graphic);
        }
        canvas.repaint();
    }
        
    private void erase() {
        Color original = graphic.getColor();
        graphic.setColor(backgroundColour);
        Dimension size = canvas.getSize();
        graphic.fill(new java.awt.Rectangle(0, 0, size.width, size.height));
        graphic.setColor(original);
    }

    private class CanvasPane extends JPanel {
        public void paint(Graphics g) {
            g.drawImage(canvasImage, 0, 0, null);
        }
    }
    
    private class ShapeDescription {
        // NOTA: Usamos java.awt.Shape en lugar de Shape para evitar la colisión
        private java.awt.Shape shape;
        private String colorString;

        public ShapeDescription(java.awt.Shape shape, String color) {
            this.shape = shape;
            colorString = color;
        }

        public void draw(Graphics2D graphic) {
            setForegroundColor(colorString);
            graphic.draw(shape);
            graphic.fill(shape);
        }
    }
}