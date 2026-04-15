package tower;

/**
 * Representa una tapa loca. Ignora la gravedad estándar y al ingresar siempre se posiciona como la base absoluta de la torre (índice 0).
 */
public class CrazyLid extends Lid {
    /**
     * Constructor de la CrazyLid.
     * @param size El tamaño lógico de la tapa.
     * @param color El color visual de la tapa.
     */
    public CrazyLid(int size, String color) { super(size, color); }
}