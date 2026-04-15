package tower;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Pruebas de integración común (Casos complejos e interacción de piezas).
 * @authors Juan Nicolás Álvarez, Leonardo Rojas
 */
public class TowerCC4Test {

    private Tower tower;

    @Before
    public void setUp() {
        tower = new Tower(500, 1000); 
    }

    @Test
    public void accordingARShouldHandleCrazyAndHierarchicalConflict() {
        // Si metemos una tapa loca, se va a la base
        tower.pushLid(2, "crazy");
        assertEquals("lid", tower.stackingitems()[0][0]);
        
        // Si metemos una hierarchical gigante, debería hundirse y desplazar a la tapa loca
        tower.pushCup(6, "hierarchical");
        String[][] items = tower.stackingitems();
        
        assertEquals("cup", items[0][0]); // La hierarchical toma el trono
        assertEquals("6", items[0][1]);
        assertEquals("lid", items[1][0]); // La tapa loca sube al segundo piso
        assertEquals("2", items[1][1]);
    }

    @Test
    public void accordingARShouldProtectFearfulFromOpenerIfUnrelated() {
        tower.pushCup(3);
        tower.pushLid(3, "fearful");
        
        // Un Opener entra y por sus reglas, DESTRUYE todas las tapas.
        // Esto pone a prueba si el "Opener" es más fuerte que el "Fearful"
        tower.pushCup(5, "opener");
        
        String[][] items = tower.stackingitems();
        assertEquals(2, items.length); // Las dos tazas, la tapa fearful fue destruida
        assertEquals("cup", items[0][0]);
        assertEquals("cup", items[1][0]);
    }
}