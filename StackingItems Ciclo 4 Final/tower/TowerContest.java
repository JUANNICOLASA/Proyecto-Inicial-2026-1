package tower;

import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JOptionPane;

/**
 * Solucionador del Problema J de la maratón ICPC (Stacking Cups).
 * @authors Juan Nicolás Álvarez, Leonardo Rojas
 */
public class TowerContest {
    private Tower tower;

    /**
     * Constructor por defecto del solucionador de ICPC. Instancia una torre base.
     */
    public TowerContest() {
        tower = new Tower(500, 1000);
    }

    /**
     * Resuelve matemáticamente el problema de sumar subconjuntos de tazas.
     * @param n El número total de tazas disponibles.
     * @param h La altura requerida que se debe alcanzar.
     * @return Una cadena de texto con la disposición requerida de las tazas separadas por espacios, o "impossible" si no hay solución.
     */
    public String solve(int n, int h) {
        long maxH = (long) n * n;
        long minH = 2L * n - 1;
        if (h < minH || h > maxH) return "impossible";

        long target1 = h - minH;
        ArrayList<Integer> subset = findSubset(target1, n - 1);
        if (subset != null) {
            return buildOutput(n, subset, 1);
        }

        long target2 = h - 1;
        subset = findSubset(target2, n - 1);
        if (subset != null) {
            return buildOutput(n, subset, 2);
        }

        return "impossible";
    }

    /**
     * Algoritmo auxiliar para hallar la combinación exacta de alturas necesarias.
     * @param V El valor delta a compensar.
     * @param k El límite de elementos evaluados.
     * @return Lista de enteros que componen el subconjunto, o null si falla.
     */
    private ArrayList<Integer> findSubset(long V, int k) {
        if (V == 0) return new ArrayList<>();
        
        for (int m = 1; m <= k; m++) {
            if ((m % 2) == (V % 2)) {
                long minSum = (long) m * m;
                long maxSum = (long) k * k - (long) (k - m) * (k - m);
                
                if (V >= minSum && V <= maxSum) {
                    ArrayList<Integer> A = new ArrayList<>();
                    for (int i = 0; i < m; i++) A.add(2 * i + 1);
                    
                    long delta = (V - minSum) / 2;
                    for (int i = m - 1; i >= 0; i--) {
                        long maxVal = 2L * k - 1 - 2L * (m - 1 - i);
                        long canAdd = (maxVal - A.get(i)) / 2;
                        long add = Math.min(delta, canAdd);
                        A.set(i, A.get(i) + (int) (2 * add));
                        delta -= add;
                    }
                    return A;
                }
            }
        }
        return null;
    }

    private String buildOutput(int n, ArrayList<Integer> subset, int method) {
        ArrayList<Integer> remaining = new ArrayList<>();
        int maxCup = 2 * n - 1;
        
        for (int i = 1; i <= n - 1; i++) {
            int cup = 2 * i - 1;
            if (!subset.contains(cup)) {
                remaining.add(cup);
            }
        }
        
        Collections.sort(remaining, Collections.reverseOrder());
        Collections.sort(subset);

        StringBuilder sb = new StringBuilder();
        if (method == 1) {
            for (int s : subset) sb.append(s).append(" ");
            sb.append(maxCup).append(" ");
            for (int r : remaining) sb.append(r).append(" ");
        } else {
            sb.append(maxCup).append(" ");
            for (int s : subset) sb.append(s).append(" ");
            for (int r : remaining) sb.append(r).append(" ");
        }
        return sb.toString().trim();
    }

    /**
     * Simula visualmente en el lienzo la solución obtenida por el método solve().
     * @param n El número total de tazas.
     * @param h La altura objetivo de la torre a simular.
     */
    public void simulate(int n, int h) {
        String sol = solve(n, h);
        if (sol.equals("impossible")) {
            JOptionPane.showMessageDialog(null, "No hay solución posible (impossible).");
            return;
        }

        if (n > 20) {
            JOptionPane.showMessageDialog(null, "Solución matemática: " + sol + 
                "\n(La cantidad de tazas es muy grande para la simulación visual)");
            return;
        }

        if (tower != null) tower.makeInvisible();
        tower = new Tower(500, 1000); 
        
        String[] parts = sol.split(" ");
        for (String p : parts) {
            tower.pushCup(Integer.parseInt(p));
        }
        tower.makeVisible();
    }
}