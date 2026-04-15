package tower;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import javax.swing.JOptionPane;

/**
 * Pruebas de aceptación.
 * @authors Juan Nicolás Álvarez, Leonardo Rojas
 */
public class TowerATest {

    private Tower tower;

    @Before
    public void setUp() {
        tower = new Tower(500, 1000); 
    }

    private void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    public void testAcceptance1_HierarchicalAndCrazy() {
        tower.makeVisible();
        delay(500);
        
        tower.pushCup(3);
        delay(1000);
        tower.pushCup(4);
        delay(1000);
        
        // La tapa Crazy se irá a la base
        tower.pushLid(5, "crazy");
        delay(1500);
        
        // La taza Hierarchical se hundirá arrastrando todo
        tower.pushCup(6, "hierarchical");
        delay(2000);
        
        int response = JOptionPane.showConfirmDialog(null, 
            "Prueba 1: ¿Viste a la tapa irse a la base, y luego a la taza gigante hundirse levantando toda la torre?", 
            "Prueba de Aceptación 1", 
            JOptionPane.YES_NO_OPTION);
            
        tower.makeInvisible();
        assertTrue("El usuario no aceptó el comportamiento de Crazy/Hierarchical.", response == JOptionPane.YES_OPTION);
    }

    @Test
    public void testAcceptance2_FearfulAndOpener() {
        tower.makeVisible();
        delay(500);
        
        tower.pushCup(4);
        delay(1000);
        
        // Entra una tapa miedosa
        tower.pushLid(4, "fearful");
        delay(1500);
        
        // Intentamos sacarla (no se dejará porque está tapando)
        tower.popLid(); 
        delay(1500);
        
        // Entra la taza destructora (Opener) y la elimina visualmente
        tower.pushCup(5, "opener");
        delay(2000);
        
        int response = JOptionPane.showConfirmDialog(null, 
            "Prueba 2: ¿Viste que la tapa miedosa no se dejó sacar con popLid(), pero fue destruida al entrar la taza Opener?", 
            "Prueba de Aceptación 2", 
            JOptionPane.YES_NO_OPTION);
            
        tower.makeInvisible();
        assertTrue("El usuario no aceptó el comportamiento de Fearful/Opener.", response == JOptionPane.YES_OPTION);
    }
}