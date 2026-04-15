package tower;

/**
 * Representa una tapa miedosa. Solo puede ingresar a la torre si su taza correspondiente del mismo tamaño ya existe.
 * Tiene la restricción de que, si actualmente está cubriendo a su taza, no se dejará extraer.
 */
public class FearfulLid extends Lid {
    /**
     * Constructor de la FearfulLid.
     * @param size El tamaño lógico de la tapa.
     * @param color El color visual de la tapa.
     */
    public FearfulLid(int size, String color) { super(size, color); }
}