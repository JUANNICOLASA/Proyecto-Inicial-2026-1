package tower;

import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JOptionPane;
import shapes.Rectangle; 

/**
 * La clase Tower es el controlador principal del simulador.
 * Administra el estado lógico, las físicas y la posición visual de todas las piezas de la torre.
 * @authors Juan Nicolás Álvarez, Leonardo Rojas
 */
public class Tower {
    private int width;
    private int maxHeight;
    private boolean isVisible;
    private ArrayList<TowerItem> items;
    private ArrayList<Rectangle> rulerMarks; 
    private boolean ok; 
    private String[] colors = {"red", "blue", "yellow", "green", "magenta"};

    /**
     * Crea una nueva torre vacía.
     * @param width El ancho máximo del lienzo.
     * @param maxHeight La altura máxima permitida para la torre.
     */
    public Tower(int width, int maxHeight) {
        this.width = width;
        this.maxHeight = maxHeight;
        this.items = new ArrayList<>();
        this.rulerMarks = new ArrayList<>();
        this.isVisible = false;
        this.ok = true;
    }

    /**
     * Crea una nueva torre pre-llenada con un número específico de tazas normales.
     * @param cups El número de tazas iniciales a generar.
     */
    public Tower(int cups) {
        this(500, 1000); 
        this.create(cups);
    }

    /**
     * Reinicia la torre y añade una cantidad secuencial de tazas normales.
     * @param number La cantidad de tazas a crear del 1 hasta n.
     */
    public void create(int number) {
        items.clear();
        for (int i = 1; i <= number; i++) pushCup(i, "normal");
        ok = true;
    }

    /**
     * Añade una taza normal de tamaño específico a la parte superior de la torre.
     * @param i El tamaño de la taza a añadir.
     */
    public void pushCup(int i) { pushCup(i, "normal"); }

    /**
     * Añade una tapa normal de tamaño específico a la parte superior de la torre.
     * @param i El tamaño de la tapa a añadir.
     */
    public void pushLid(int i) { pushLid(i, "normal"); }

    /**
     * Añade una taza a la torre, instanciando un comportamiento específico si se solicita.
     * @param i El tamaño de la taza.
     * @param type El tipo especial ("normal", "opener", "hierarchical").
     */
    public void pushCup(int i, String type) {
        Cup newCup;
        if (type.equals("opener")) {
            newCup = new OpenerCup(i, getColorForSize(i));
            
            for(TowerItem item : items) {
                if(item instanceof Lid) item.setVisible(false);
            }
            
            items.removeIf(item -> item instanceof Lid); 
        } else if (type.equals("hierarchical")) {
            newCup = new HierarchicalCup(i, getColorForSize(i));
        } else {
            newCup = new Cup(i, getColorForSize(i));
        }
        
        items.add(newCup);
        
        if (type.equals("hierarchical")) {
            int idx = items.size() - 1;
            while (idx > 0 && items.get(idx - 1).getSize() < i) {
                Collections.swap(items, idx, idx - 1);
                idx--;
            }
        }
        ok = true;
        refreshView();
    }

    /**
     * Añade una tapa a la torre, instanciando un comportamiento específico si se solicita.
     * @param i El tamaño de la tapa.
     * @param type El tipo especial ("normal", "fearful", "crazy").
     */
    public void pushLid(int i, String type) {
        if (type.equals("fearful")) {
            boolean hasPartner = items.stream().anyMatch(item -> item instanceof Cup && item.getSize() == i);
            if (!hasPartner) { 
                ok = false; 
                return; 
            }
            items.add(new FearfulLid(i, getColorForSize(i)));
        } else if (type.equals("crazy")) {
            items.add(0, new CrazyLid(i, getColorForSize(i)));
        } else {
            items.add(new Lid(i, getColorForSize(i)));
        }
        ok = true;
        refreshView();
    }

    /**
     * Intercambia la posición de dos piezas en la torre dadas sus coordenadas en la lista.
     * @param i Índice de la primera pieza.
     * @param j Índice de la segunda pieza.
     */
    public void swap(int i, int j) {
        if (i >= 0 && i < items.size() && j >= 0 && j < items.size()) {
            Collections.swap(items, i, j);
            ok = true;
            refreshView();
        } else {
            ok = false;
        }
    }

    /**
     * Comprueba si un ítem tapa coincide en tamaño y tipo con un ítem taza.
     */
    private boolean isMatch(TowerItem lid, TowerItem cup) {
        return lid instanceof Lid && cup instanceof Cup && lid.getSize() == cup.getSize();
    }

    /**
     * Reorganiza la torre buscando activamente tapas para cubrir las tazas compatibles.
     */
    public void cover() {
        for (int i = 0; i < items.size(); i++) {
            TowerItem current = items.get(i);
            if (current instanceof Cup) { 
                if (i + 1 < items.size() && isMatch(items.get(i+1), current)) {
                    continue;
                }
                for (int j = 0; j < items.size(); j++) {
                    TowerItem potentialLid = items.get(j);
                    if (isMatch(potentialLid, current)) { 
                        boolean isBusy = (j > 0 && isMatch(potentialLid, items.get(j-1)));
                        if (!isBusy) {
                            TowerItem lid = items.remove(j);
                            int insertPos = (j > i) ? i + 1 : i; 
                            items.add(insertPos, lid);
                            break;
                        }
                    }
                }
            }
        }
        ok = true;
        refreshView();
    }

    /**
     * Busca el primer intercambio de piezas adyacentes que resulte en una reducción de la altura total.
     * @return Arreglo bidimensional de Strings con los datos de las dos piezas intercambiadas. Vacío si no hay reducción.
     */
    public String[][] swapToReduce() {
        int currentHeight = height();
        for (int i = 0; i < items.size() - 1; i++) {
            int j = i + 1; 
            Collections.swap(items, i, j);
            int newHeight = height();
            Collections.swap(items, i, j); 
            if (newHeight < currentHeight) {
                String[][] result = new String[2][2];
                String typeI = (items.get(i) instanceof Cup) ? "cup" : "lid";
                String typeJ = (items.get(j) instanceof Cup) ? "cup" : "lid";
                
                result[0] = new String[]{typeI, String.valueOf(items.get(i).getSize())};
                result[1] = new String[]{typeJ, String.valueOf(items.get(j).getSize())};
                
                Collections.swap(items, i, j); 
                ok = true;
                refreshView(); 
                return result; 
            }
        }
        ok = false; 
        return new String[0][0]; 
    }

    /**
     * Retira la taza que se encuentre más en la superficie (la más alta) de la pila.
     */
    public void popCup() {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (items.get(i) instanceof Cup) {
                if (items.get(i) instanceof HierarchicalCup && i == 0) {
                    ok = false; return;
                }
                items.get(i).setVisible(false);
                items.remove(i);
                ok = true;
                refreshView();
                return;
            }
        }
        ok = false;
    }

    /**
     * Retira la tapa que se encuentre más en la superficie (la más alta) de la pila.
     */
    public void popLid() {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (items.get(i) instanceof Lid) {
                if (items.get(i) instanceof FearfulLid && checkIfLidIsCovering(i)) {
                    ok = false; return;
                }
                items.get(i).setVisible(false);
                items.remove(i);
                ok = true;
                refreshView();
                return;
            }
        }
        ok = false;
    }
    
    /**
     * Retira una taza específica en función de su tamaño.
     * @param size El tamaño de la taza a retirar.
     */
    public void removeCup(int size) {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (items.get(i) instanceof Cup && items.get(i).getSize() == size) {
                if (items.get(i) instanceof HierarchicalCup && i == 0) {
                    ok = false; return;
                }
                items.get(i).setVisible(false);
                items.remove(i);
                ok = true;
                refreshView();
                return;
            }
        }
        ok = false;
    }
    
    /**
     * Retira una tapa específica en función de su tamaño.
     * @param size El tamaño de la tapa a retirar.
     */
    public void removeLid(int size) {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (items.get(i) instanceof Lid && items.get(i).getSize() == size) {
                if (items.get(i) instanceof FearfulLid && checkIfLidIsCovering(i)) {
                    ok = false; return;
                }
                items.get(i).setVisible(false);
                items.remove(i);
                ok = true;
                refreshView();
                return;
            }
        }
        ok = false;
    }

    /**
     * Valida si una tapa específica en la pila está actualmente tapando a una taza.
     */
    private boolean checkIfLidIsCovering(int lidIndex) {
        TowerItem lid = items.get(lidIndex);
        for (int k = lidIndex - 1; k >= 0; k--) {
            TowerItem below = items.get(k);
            if (below.getSize() >= lid.getSize()) {
                if (below instanceof Cup && below.getSize() == lid.getSize()) return true;
                break;
            }
        }
        return false;
    }

    /**
     * Ordena la torre colocando las piezas más grandes en la base y anidando adecuadamente tazas y tapas.
     */
    public void orderTower() {
        items.sort((o1, o2) -> Integer.compare(o2.getSize(), o1.getSize()));
        for(int i = 0; i < items.size() - 1; i++){
            TowerItem current = items.get(i);
            TowerItem next = items.get(i+1);
            if(current.getSize() == next.getSize() && current instanceof Lid && next instanceof Cup){
                Collections.swap(items, i, i+1);
            }
        }
        ok = true;
        refreshView();
    }

    /**
     * Invierte el orden lógico y físico de la torre.
     */
    public void reverseTower() {
        Collections.reverse(items);
        ok = true;
        refreshView();
    }

    /**
     * Calcula la altura final acumulada de todos los ítems apilados dadas las físicas de anidamiento.
     * @return La altura total de la torre en píxeles.
     */
    public int height() {
        int h = 0;
        TowerItem previous = null;
        for(TowerItem item : items){
            h += item.getAddedHeightWhenPlacedOn(previous);
            previous = item;
        }
        return h;
    }

    /**
     * Retorna una lista con los tamaños de las tazas que están actualmente selladas por su respectiva tapa.
     * @return Un arreglo de enteros con los tamaños.
     */
    public int[] lidedCups() {
        ArrayList<Integer> lided = new ArrayList<>();
        for(int i = 0; i < items.size() - 1; i++){
            if (isMatch(items.get(i+1), items.get(i))) {
                lided.add(items.get(i).getSize());
            }
        }
        int[] result = new int[lided.size()];
        for(int i=0; i<lided.size(); i++) result[i] = lided.get(i);
        return result;
    }

    /**
     * Genera una representación tabular de los objetos actualmente en la pila.
     * @return Matriz bidimensional [i][0=tipo, 1=tamaño].
     */
    public String[][] stackingitems() {
        String[][] result = new String[items.size()][2];
        for(int i=0; i<items.size(); i++){
            String type = (items.get(i) instanceof Cup) ? "cup" : "lid";
            result[i] = new String[]{type, String.valueOf(items.get(i).getSize())};
        }
        return result;
    }

    /**
     * Dibuja y hace visible la torre y sus elementos en el lienzo. Verifica las restricciones de altura máxima.
     */
    public void makeVisible() {
        if (height() > maxHeight) {
            isVisible = false;
            showMessage("La torre excede la altura máxima permitida.");
        } else {
            isVisible = true;
            refreshView();
        }
    }

    /**
     * Oculta el componente visual de la torre y elimina la regla de medición.
     */
    public void makeInvisible() {
        isVisible = false;
        for(TowerItem item : items) item.setVisible(false);
        for(Rectangle r : rulerMarks) r.makeInvisible();
    }
    
    /**
     * Cierra el simulador.
     */
    public void exit() { System.exit(0); }
    
    /**
     * Retorna el estado de éxito o fracaso de la última acción ejecutada.
     * @return true si la acción fue exitosa, false de lo contrario.
     */
    public boolean ok() { return ok; }
    
    /**
     * Actualiza el lienzo volviendo a calcular las posiciones lógicas en cascada.
     */
    private void refreshView() {
        if(!isVisible) return;
        int centerX = 150; 
        TowerItem previous = null;
        
        boolean[] coveredStates = new boolean[items.size()];
        
        for (int i = 0; i < items.size(); i++) {
            TowerItem item = items.get(i);
            boolean isCovered = false;
            
            if (item instanceof Cup) {
                for (int k = i + 1; k < items.size(); k++) {
                    TowerItem blocker = items.get(k);
                    if (blocker.getSize() >= item.getSize()) {
                        if (blocker instanceof Lid && blocker.getSize() == item.getSize()) {
                            isCovered = true;
                        }
                        break; 
                    }
                }
            }
            coveredStates[i] = isCovered;
            item.setCovered(isCovered); 
        }
        
        for (int i = 0; i < items.size(); i++) {
            TowerItem item = items.get(i);
            if (item instanceof Cup) {
                boolean hasSealedContent = false;
                if (coveredStates[i]) {
                    for (int j = i + 1; j < items.size(); j++) {
                        TowerItem nested = items.get(j);
                        if (nested.getSize() >= item.getSize()) {
                            break; 
                        }
                        if (nested instanceof Cup && coveredStates[j]) {
                            hasSealedContent = true;
                            break; 
                        }
                    }
                }
                ((Cup) item).setFullySealed(hasSealedContent); 
            }
        }
        
        int currentBottom = 300; 
        int currentTop = 300;    
        
        for (int i = 0; i < items.size(); i++) {
            TowerItem item = items.get(i);
            int yPos = 300;
            
            if (previous == null) {
                yPos = 300 - item.getHeight();
                currentBottom = 300;
                currentTop = yPos;
            } else {
                boolean isNestingCup = item instanceof Cup && previous instanceof Cup && previous.getSize() > item.getSize();
                boolean isNestingLid = item instanceof Lid && previous instanceof Cup && previous.getSize() > item.getSize();
                boolean isPerfectLid = isMatch(item, previous);
                
                if (isNestingCup || isNestingLid) {
                    currentBottom -= 3; 
                    yPos = currentBottom - item.getHeight();
                    currentTop = yPos; 
                } else if (isPerfectLid) {
                    currentBottom = currentTop;
                    yPos = currentBottom - item.getHeight();
                    currentTop = yPos;
                } else {
                    currentBottom = currentTop; 
                    yPos = currentBottom - item.getHeight();
                    currentTop = yPos;
                }
            }
            
            item.setPosition(centerX, yPos); 
            previous = item;
        }
        
        for(Rectangle r : rulerMarks) r.makeInvisible();
        rulerMarks.clear();
        
        int baseRulerY = 300;
        int topRulerY = currentTop; 
        
        for (int y = baseRulerY; y >= topRulerY - 10; y -= 10) {
            Rectangle tick = new Rectangle();
            tick.changeColor("black");
            tick.changeSize(2, 10); 
            tick.moveHorizontal(20 - 70); 
            tick.moveVertical(y - 15);
            tick.makeVisible();
            rulerMarks.add(tick);
        }
    }

    private String getColorForSize(int i){
        return colors[(i > 0 ? i - 1 : 0) % colors.length];
    }
    
    private void showMessage(String msg){
        if(isVisible) JOptionPane.showMessageDialog(null, msg);
    }
}