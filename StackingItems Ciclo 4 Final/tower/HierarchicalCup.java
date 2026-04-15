package tower;

/**
 * Representa una taza jerárquica. Al ingresar, se hunde hasta el fondo desplazando a cualquier elemento de menor tamaño.
 * Posee la restricción de que si alcanza la base (índice 0), no puede ser extraída de la torre.
 */
public class HierarchicalCup extends Cup {
    /**
     * Constructor de la HierarchicalCup.
     * @param size El tamaño lógico de la taza.
     * @param color El color visual de la taza.
     */
    public HierarchicalCup(int size, String color) { super(size, color); }
}