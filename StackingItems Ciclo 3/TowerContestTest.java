import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Pruebas unitarias para el Solver del Ciclo 3.
 * @authors Juan Nicolás Álvarez, Leonardo Rojas
 */
public class TowerContestTest {
    
    @Test
    public void accordingARShouldSolveTargetHeights() {
        TowerContest contest = new TowerContest();
        assertEquals("7 3 5 1", contest.solve(4, 9));
        
        assertEquals("1 3 5 7", contest.solve(4, 16));
        
        assertEquals("7 5 3 1", contest.solve(4, 7));
    }
    
    @Test
    public void accordingARShouldDetectImpossibleHeights() {
        TowerContest contest = new TowerContest();
        
        assertEquals("impossible", contest.solve(4, 5)); 
        
        assertEquals("impossible", contest.solve(4, 25)); 
        
        assertEquals("impossible", contest.solve(4, 14)); 
    }
}