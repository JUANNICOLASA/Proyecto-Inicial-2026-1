import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JOptionPane;

/**
 * La clase Tower es el controlador principal del simulador Stacking Cups.
 * Cumple con los requisitos del Ciclo 1 y las extensiones del Ciclo 2,
 * gestionando el ordenamiento, validación y dibujado de las tazas y tapas.
 * * @author Juan Nicolás Álvarez, Leonardo Rojas
 * @version 2.0 (Ciclo 2)
 */
public class Tower {
    private int width;
    private int maxHeight;
    private boolean isVisible;
    private ArrayList<Object> items; 
    private boolean ok; 
    
    private String[] colors = {"red", "blue", "yellow", "green", "magenta"};

    /**
     * Crea una torre especificando sus límites físicos de simulación.
     * * @param width     Ancho de la torre.
     * @param maxHeight Altura máxima permitida antes de mostrar una advertencia.
     */
    public Tower(int width, int maxHeight) {
        this.width = width;
        this.maxHeight = maxHeight;
        this.items = new ArrayList<>();
        this.isVisible = false;
        this.ok = true;
    }

    /**
     * REQUISITO 10: Crea una torre con el número indicado de tazas (de tamaño 1 a N).
     * Reemplaza cualquier elemento previamente existente en la torre.
     * * @param number La cantidad de tazas consecutivas a generar.
     */
    public void create(int number) {
        items.clear();
        for (int i = 1; i <= number; i++) {
            pushCup(i);
        }
        ok = true;
    }

    /**
     * REQUISITO 11: Intercambia la posición lógica y visual de dos objetos en la torre.
     * * @param i Índice del primer objeto (base = 0).
     * @param j Índice del segundo objeto.
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
     * REQUISITO 12: Reorganiza la torre buscando las tapas que corresponden a las 
     * tazas actuales y colocándolas directamente sobre ellas.
     */
    public void cover() {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) instanceof Cup) {
                Cup cup = (Cup) items.get(i);
                for (int j = 0; j < items.size(); j++) {
                    if (items.get(j) instanceof Lid && ((Lid)items.get(j)).getSize() == cup.getSize()) {
                        if (j != i + 1) {
                            Object lid = items.remove(j);
                            int insertPos = (j > i) ? i + 1 : i; 
                            items.add(insertPos, lid);
                        }
                        break;
                    }
                }
            }
        }
        ok = true;
        refreshView();
    }

    /**
     * REQUISITO 13: Consulta y evalúa un movimiento de intercambio (swap) que resulte 
     * en una reducción de la altura total de la torre, simulando el acople de piezas.
     * * @return Una matriz bidimensional String[][] indicando el tipo y tamaño de los dos 
     * objetos a intercambiar. Retorna una matriz vacía si no hay optimización posible.
     */
    public String[][] swapToReduce() {
        int currentHeight = height();
        
        for (int i = 0; i < items.size() - 1; i++) {
            for (int j = i + 1; j < items.size(); j++) {
                Collections.swap(items, i, j);
                int newHeight = height();
                Collections.swap(items, i, j);
                
                if (newHeight < currentHeight) {
                    String[][] result = new String[2][2];
                    result[0] = getItemInfo(items.get(i));
                    result[1] = getItemInfo(items.get(j));
                    ok = true;
                    return result;
                }
            }
        }
        ok = false;
        return new String[0][0]; 
    }

    /**
     * Helper para formatear la salida estructurada de los objetos consultados.
     * * @param o Objeto de tipo Cup o Lid.
     * @return Arreglo de String con el formato ["tipo", "tamaño"].
     */
    private String[] getItemInfo(Object o) {
        if (o instanceof Cup) return new String[]{"cup", String.valueOf(((Cup)o).getSize())};
        else return new String[]{"lid", String.valueOf(((Lid)o).getSize())};
    }

    /**
     * Agrega una taza a la parte superior de la torre si no existe otra de igual tamaño.
     * * @param i Tamaño de la taza.
     */
    public void pushCup(int i) {
        if (existsItem(i, Cup.class)) { ok = false; return; }
        items.add(new Cup(i, getColorForSize(i)));
        ok = true;
        refreshView();
    }

    /**
     * Agrega una tapa a la parte superior de la torre si no existe otra de igual tamaño.
     * * @param i Tamaño de la tapa.
     */
    public void pushLid(int i) {
        if (existsItem(i, Lid.class)) { ok = false; return; }
        items.add(new Lid(i, getColorForSize(i)));
        ok = true;
        refreshView();
    }

    /**
     * Elimina el elemento de tipo Cup que se encuentre más arriba en la estructura.
     */
    public void popCup() {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (items.get(i) instanceof Cup) {
                ((Cup)items.get(i)).setVisible(false);
                items.remove(i);
                ok = true;
                refreshView();
                return;
            }
        }
        ok = false;
    }

    /**
     * Elimina el elemento de tipo Lid que se encuentre más arriba en la estructura.
     */
    public void popLid() {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (items.get(i) instanceof Lid) {
                ((Lid)items.get(i)).getVisual().makeInvisible();
                items.remove(i);
                ok = true;
                refreshView();
                return;
            }
        }
        ok = false;
    }
    
    /**
     * Elimina específicamente una taza dado su tamaño.
     * * @param size El tamaño de la taza a eliminar.
     */
    public void removeCup(int size) {
        boolean removed = false;
        for(int i = 0; i < items.size(); i++){
            Object o = items.get(i);
            if(o instanceof Cup && ((Cup)o).getSize() == size){
                ((Cup)o).setVisible(false);
                items.remove(i);
                removed = true;
                break;
            }
        }
        if(removed) { refreshView(); ok = true; } else { ok = false; }
    }
    
    /**
     * Elimina específicamente una tapa dado su tamaño.
     * * @param size El tamaño de la tapa a eliminar.
     */
    public void removeLid(int size) {
        boolean removed = false;
        for(int i = 0; i < items.size(); i++){
            Object o = items.get(i);
            if(o instanceof Lid && ((Lid)o).getSize() == size){
                ((Lid)o).getVisual().makeInvisible();
                items.remove(i);
                removed = true;
                break;
            }
        }
        if(removed) { refreshView(); ok = true; } else { ok = false; }
    }

    /**
     * Ordena la torre de forma descendente (bases mayores abajo) y garantiza 
     * que las tapas se acoplen a las tazas correspondientes.
     */
    public void orderTower() {
        items.sort((o1, o2) -> Integer.compare(getSize(o2), getSize(o1)));
        for(int i = 0; i < items.size() - 1; i++){
            if(getSize(items.get(i)) == getSize(items.get(i+1)) && items.get(i) instanceof Lid && items.get(i+1) instanceof Cup){
                Collections.swap(items, i, i+1);
            }
        }
        ok = true;
        refreshView();
    }

    /**
     * Invierte completamente el orden actual de todos los elementos de la torre.
     */
    public void reverseTower() {
        Collections.reverse(items);
        ok = true;
        refreshView();
    }

    /**
     * Calcula la altura real ocupada por los elementos de la torre.
     * Las tapas correctamente colocadas sobre su respectiva taza no suman altura extra.
     * * @return Altura acumulada en píxeles.
     */
    public int height() {
        int h = 0;
        for(int i = 0; i < items.size(); i++){
            Object o = items.get(i);
            if(o instanceof Cup) {
                h += ((Cup)o).getHeight();
            } else if(o instanceof Lid) {
                if (i > 0 && items.get(i-1) instanceof Cup && ((Cup)items.get(i-1)).getSize() == ((Lid)o).getSize()) {
                } else {
                    h += ((Lid)o).getHeight();
                }
            }
        }
        return h;
    }

    /**
     * Consulta cuáles tazas tienen su respectiva tapa inmediatamente encima.
     * * @return Arreglo de enteros con los tamaños de las tazas que están correctamente tapadas.
     */
    public int[] lidedCups() {
        ArrayList<Integer> lided = new ArrayList<>();
        for(int i = 0; i < items.size() - 1; i++){
            if(items.get(i) instanceof Cup && items.get(i+1) instanceof Lid){
                if(((Cup)items.get(i)).getSize() == ((Lid)items.get(i+1)).getSize()){
                    lided.add(((Cup)items.get(i)).getSize());
                }
            }
        }
        int[] result = new int[lided.size()];
        for(int i=0; i<lided.size(); i++) result[i] = lided.get(i);
        return result;
    }

    /**
     * Genera una representación tabular del estado actual de la torre.
     * * @return Matriz de strings con el tipo de objeto en la primera columna y su tamaño en la segunda.
     */
    public String[][] stackingitems() {
        String[][] result = new String[items.size()][2];
        for(int i=0; i<items.size(); i++){
            result[i] = getItemInfo(items.get(i));
        }
        return result;
    }

    /**
     * Evalúa las restricciones de límite de altura y hace visible la torre en pantalla.
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
     * Oculta gráficamente todos los elementos que componen la torre.
     */
    public void makeInvisible() {
        isVisible = false;
        for(Object o : items){
            if(o instanceof Cup) ((Cup)o).setVisible(false);
            if(o instanceof Lid) ((Lid)o).getVisual().makeInvisible();
        }
    }
    
    /**
     * Termina la ejecución del programa.
     */
    public void exit() { System.exit(0); }
    
    /**
     * Retorna el estado de completitud o éxito del último comando ejecutado.
     * * @return true si la operación fue válida y exitosa, false en caso contrario.
     */
    public boolean ok() { return ok; }
    
    /**
     * Reposiciona matemáticamente todos los componentes en el lienzo respetando
     * las reglas físicas (las tapas encajan en las tazas y el color cambia si están tapadas).
     */
    private void refreshView() {
        if(!isVisible) return;
        
        int currentY = 300; 
        int centerX = 150; 
        
        for (int i = 0; i < items.size(); i++) {
            Object item = items.get(i);
            
            if (item instanceof Cup) {
                Cup c = (Cup) item;
                boolean isCovered = (i + 1 < items.size() && items.get(i+1) instanceof Lid && ((Lid)items.get(i+1)).getSize() == c.getSize());
                c.setCovered(isCovered);
                
                currentY -= c.getHeight();
                c.setPosition(centerX, currentY); 
                
            } else if (item instanceof Lid) {
                Lid l = (Lid) item;
                if (i > 0 && items.get(i-1) instanceof Cup && ((Cup)items.get(i-1)).getSize() == l.getSize()) {
                    l.setPosition(centerX, currentY);
                } else {
                    currentY -= l.getHeight();
                    l.setPosition(centerX, currentY);
                }
            }
        }
    }

    /**
     * Valida la unicidad de los elementos dentro de la estructura.
     * * @param size El tamaño numérico a validar.
     * @param type La clase del objeto (Cup.class o Lid.class).
     * @return true si el objeto ya existe, false en caso contrario.
     */
    private boolean existsItem(int size, Class<?> type) {
        for (Object o : items) {
            if (type.isInstance(o)) {
                if (o instanceof Cup && ((Cup) o).getSize() == size) return true;
                if (o instanceof Lid && ((Lid) o).getSize() == size) return true;
            }
        }
        return false;
    }
    
    private int getSize(Object o){
        if(o instanceof Cup) return ((Cup)o).getSize();
        if(o instanceof Lid) return ((Lid)o).getSize();
        return 0;
    }
    
    private String getColorForSize(int i){
        int index = (i > 0) ? (i - 1) : 0;
        return colors[index % colors.length];
    }
    
    private void showMessage(String msg){
        if(isVisible){
            JOptionPane.showMessageDialog(null, msg);
        }
    }
}