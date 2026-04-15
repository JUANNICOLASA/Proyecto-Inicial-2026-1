package shapes;

/**
 * Clase abstracta que define las propiedades y comportamientos básicos de una figura geométrica.
 * Proporciona métodos para mover, mostrar y cambiar el color de la figura en el lienzo.
 */
public abstract class Shape {
    protected int xPosition;
    protected int yPosition;
    protected String color;
    protected boolean isVisible;

    /**
     * Hace que la figura sea visible en el lienzo.
     */
    public void makeVisible() {
        isVisible = true;
        draw();
    }

    /**
     * Hace que la figura sea invisible en el lienzo.
     */
    public void makeInvisible() {
        erase();
        isVisible = false;
    }

    /**
     * Mueve la figura unos pocos píxeles hacia la derecha.
     */
    public void moveRight() { moveHorizontal(20); }

    /**
     * Mueve la figura unos pocos píxeles hacia la izquierda.
     */
    public void moveLeft() { moveHorizontal(-20); }

    /**
     * Mueve la figura unos pocos píxeles hacia arriba.
     */
    public void moveUp() { moveVertical(-20); }

    /**
     * Mueve la figura unos pocos píxeles hacia abajo.
     */
    public void moveDown() { moveVertical(20); }

    /**
     * Mueve la figura horizontalmente una distancia específica.
     * @param distance La distancia en píxeles que se moverá la figura (positivo para derecha, negativo para izquierda).
     */
    public void moveHorizontal(int distance) {
        erase();
        xPosition += distance;
        draw();
    }

    /**
     * Mueve la figura verticalmente una distancia específica.
     * @param distance La distancia en píxeles que se moverá la figura (positivo para abajo, negativo para arriba).
     */
    public void moveVertical(int distance) {
        erase();
        yPosition += distance;
        draw();
    }

    /**
     * Cambia el color de la figura.
     * @param newColor El nuevo color en formato de cadena (ej. "red", "blue").
     */
    public void changeColor(String newColor) {
        color = newColor;
        draw();
    }

    /**
     * Dibuja la figura en el lienzo. Debe ser implementado por las subclases.
     */
    protected abstract void draw();

    /**
     * Borra la figura del lienzo. Debe ser implementado por las subclases.
     */
    protected abstract void erase();
}