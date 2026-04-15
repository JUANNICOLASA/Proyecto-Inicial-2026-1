package tower;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Suite de pruebas unitarias para validar la extensión del Ciclo 2 
 * del simulador Stacking Cups en modo invisible.
 * * @author Juan Nicolás Álvarez, Leonardo Rojas
 * @version 2.0 (Ciclo 2)
 */
public class TowerC2Test {

    private Tower tower;

    /**
     * Configuración inicial para cada prueba unitaria.
     * Se instancia la torre asegurando que se ejecute en "modo invisible" 
     * como lo estipula el requisito de entrega.
     */
    @Before
    public void setUp() {
        tower = new Tower(500, 500); 
    }

    /**
     * Prueba que el sistema genere exitosamente una torre iterativa de tazas 
     * utilizando el nuevo método create.
     */
    @Test
    public void accordingARShouldCreateTower() {
        tower.create(5);
        assertEquals(5, tower.stackingitems().length);
        assertTrue(tower.ok());
    }

    /**
     * Prueba la robustez del sistema validando que NO lance excepciones no controladas 
     * al intentar hacer swap con índices fuera del tamaño de la lista.
     */
    @Test
    public void accordingARShouldNotSwapInvalidIndices() {
        tower.create(2);
        tower.swap(0, 5); 
        assertFalse(tower.ok()); 
    }

    /**
     * Prueba que el sistema permita permutar correctamente la posición 
     * lógica de dos elementos de la torre.
     */
    @Test
    public void accordingARShouldSwapValidPositions() {
        tower.pushCup(1);
        tower.pushLid(2);
        tower.swap(0, 1);
        String[][] items = tower.stackingitems();
        assertEquals("lid", items[0][0]);
        assertEquals("cup", items[1][0]);
    }

    /**
     * Prueba que el sistema busque activamente las tapas compatibles en la estructura 
     * y las posiciones de manera automática sobre sus tazas.
     */
    @Test
    public void accordingARShouldCoverMatchingCups() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushLid(1); 
        assertEquals(0, tower.lidedCups().length);
        
        tower.cover();
        
        assertEquals(1, tower.lidedCups().length);
        assertEquals(1, tower.lidedCups()[0]);
    }

    /**
     * Prueba algorítmica de la función consultiva que verifica escenarios de 
     * optimización de altura evaluando permutaciones.
     */
    @Test
    public void accordingARShouldReturnSwapToReduceHeight() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushLid(1); 
        
        String[][] move = tower.swapToReduce();
        
        assertNotNull(move);
        assertEquals(2, move.length);
        
        assertEquals("cup", move[0][0]);
        assertEquals("1", move[0][1]);
        assertEquals("cup", move[1][0]);
        assertEquals("2", move[1][1]);
    }
    
    /**
     * Prueba de frontera que asegura que el sistema NO invente movimientos 
     * cuando la torre ya está optimizada o compactada al máximo de su capacidad.
     */
    @Test
    public void accordingARShouldNotReduceHeightIfAlreadyOptimized() {
        tower.pushCup(1);
        tower.pushLid(1); 
        tower.pushCup(2);
        tower.pushLid(2); 
        
        String[][] move = tower.swapToReduce();
        
        assertEquals(0, move.length);
    }
}
