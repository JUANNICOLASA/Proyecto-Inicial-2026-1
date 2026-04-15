package tower;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Suite de pruebas de unidad para validar la extensión del Ciclo 4.
 * Evalúa los comportamientos individuales de las piezas especiales.
 * @authors Juan Nicolás Álvarez, Leonardo Rojas
 */
public class TowerC4Test {

    private Tower tower;

    @Before
    public void setUp() {
        // Inicializamos la torre en modo invisible para pruebas rápidas
        tower = new Tower(500, 1000); 
    }

    @Test
    public void accordingARShouldDestroyLidsWithOpener() {
        tower.pushCup(2);
        tower.pushLid(2); // Ponemos una tapa
        assertEquals(2, tower.stackingitems().length);
        
        // El opener debe destruir la tapa existente
        tower.pushCup(4, "opener");
        String[][] items = tower.stackingitems();
        
        assertEquals(2, items.length); // Solo deben quedar las 2 tazas
        assertEquals("cup", items[0][0]); 
        assertEquals("cup", items[1][0]); 
        assertTrue(tower.ok());
    }

    @Test
    public void accordingARShouldSinkHierarchicalAndPreventRemoval() {
        tower.pushCup(2);
        tower.pushCup(3);
        
        // La taza hierarchical (talla 5) debe hundirse hasta la base
        tower.pushCup(5, "hierarchical");
        String[][] items = tower.stackingitems();
        
        assertEquals("5", items[0][1]); // Debe estar en el índice 0 (la base)
        
        // Intentar eliminar la taza hierarchical desde el fondo debe fallar
        tower.removeCup(5);
        assertFalse(tower.ok());
        assertEquals(3, tower.stackingitems().length); // Sigue ahí
    }

    @Test
    public void accordingARShouldRestrictFearfulLid() {
        // 1. No debe entrar si no está su taza
        tower.pushLid(4, "fearful");
        assertFalse(tower.ok());
        
        // 2. Debe entrar si su taza existe
        tower.pushCup(4);
        tower.pushLid(4, "fearful");
        assertTrue(tower.ok());
        
        // 3. No debe salir si está tapando a su taza
        tower.popLid();
        assertFalse(tower.ok());
    }

    @Test
    public void accordingARShouldPutCrazyLidAtBase() {
        tower.pushCup(3);
        tower.pushCup(4);
        
        // La tapa loca debe irse al fondo (índice 0)
        tower.pushLid(5, "crazy");
        String[][] items = tower.stackingitems();
        
        assertEquals("lid", items[0][0]);
        assertEquals("5", items[0][1]);
        assertTrue(tower.ok());
    }
}