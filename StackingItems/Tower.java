import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JOptionPane;

/**
 * La clase Tower es el controlador principal del simulador Stacking Cups.
 * Gestiona una colección de tazas (Cup) y tapas (Lid), permitiendo apilarlas,
 * ordenarlas y visualizarlas en el Canvas.
 * * @authors Juan Nicolás Álvarez, Leonardo Rojas
 * @version 1.0 (Ciclo 1)
 */
public class Tower {
    private int width;
    private int maxHeight;
    private boolean isVisible;
    private ArrayList<Object> items; // Contenedor polimórfico para Cup y Lid
    private boolean ok; // Estado de la última operación
    
    // Paleta de colores cíclica: 0=red, 1=blue, 2=yellow, etc.
    private String[] colors = {"red", "blue", "yellow", "green", "magenta", "black"};

    /**
     * Constructor para crear una nueva torre vacía.
     * * @param width El ancho máximo disponible.
     * @param maxHeight La altura máxima permitida para la torre.
     */
    public Tower(int width, int maxHeight) {
        this.width = width;
        this.maxHeight = maxHeight;
        this.items = new ArrayList<>();
        this.isVisible = false;
        this.ok = true;
    }

    /**
     * Adiciona una taza a la torre si no existe ya una de ese tamaño.
     * * @param i El tamaño de la taza a adicionar.
     */
    public void pushCup(int i) {
        if (existsItem(i, Cup.class)) {
            ok = false;
            showMessage("La taza " + i + " ya existe.");
            return;
        }
        
        // Asignar color y crear taza
        String color = getColorForSize(i);
        Cup newCup = new Cup(i, color);
        items.add(newCup);
        
        ok = true;
        refreshView();
    }

    /**
     * Adiciona una tapa a la torre si no existe ya una de ese tamaño.
     * * @param i El tamaño de la tapa a adicionar.
     */
    public void pushLid(int i) {
         if (existsItem(i, Lid.class)) {
            ok = false;
            showMessage("La tapa " + i + " ya existe.");
            return;
        }
        
        String color = getColorForSize(i);
        Lid newLid = new Lid(i, color);
        items.add(newLid);
        
        ok = true;
        refreshView();
    }

    /**
     * Elimina la última taza agregada (o la que esté más arriba en la pila).
     * Si no hay tazas, muestra un mensaje de error.
     */
    public void popCup() {
        // Buscar desde el final hacia el principio
        for (int i = items.size() - 1; i >= 0; i--) {
            if (items.get(i) instanceof Cup) {
                Cup c = (Cup) items.get(i);
                c.setVisible(false); // Ocultar visualmente
                items.remove(i);     // Eliminar de la lista
                ok = true;
                refreshView();
                return;
            }
        }
        ok = false;
        showMessage("No hay tazas para eliminar.");
    }

    /**
     * Elimina la última tapa agregada.
     * Si no hay tapas, muestra un mensaje de error.
     */
    public void popLid() {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (items.get(i) instanceof Lid) {
                Lid l = (Lid) items.get(i);
                l.getVisual().makeInvisible();
                items.remove(i);
                ok = true;
                refreshView();
                return;
            }
        }
        ok = false;
        showMessage("No hay tapas para eliminar.");
    }
    
    /**
     * Elimina una taza de un tamaño específico.
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
        
        if(removed) {
             refreshView();
             ok = true;
        } else {
            ok = false;
        }
    }
    
    /**
     * Elimina una tapa de un tamaño específico.
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
        
        if(removed) {
             refreshView();
             ok = true;
        } else {
            ok = false;
        }
    }

    /**
     * Ordena la torre de mayor a menor tamaño (base más grande abajo).
     * Además, asegura que si una taza tiene su tapa correspondiente, 
     * la tapa se coloque inmediatamente encima de la taza.
     */
    public void orderTower() {
        // 1. Ordenar por tamaño descendente
        items.sort(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                int s1 = getSize(o1);
                int s2 = getSize(o2);
                return Integer.compare(s2, s1); 
            }
        });
        
        // 2. Ajuste: Tapa sobre Taza del mismo tamaño
        for(int i = 0; i < items.size() - 1; i++){
            Object bottom = items.get(i);
            Object top = items.get(i+1);
            
            // Si tienen el mismo tamaño
            if(getSize(bottom) == getSize(top)){
                // Y el orden es incorrecto (Lid abajo, Cup arriba) -> Intercambiar
                if(bottom instanceof Lid && top instanceof Cup){
                    items.set(i, top);
                    items.set(i+1, bottom);
                }
            }
        }
        
        ok = true;
        refreshView();
    }

    /**
     * Invierte el orden actual de los elementos en la torre.
     */
    public void reverseTower() {
        Collections.reverse(items);
        ok = true;
        refreshView();
    }

    /**
     * Calcula la altura total de los elementos apilados.
     * * @return La altura total en píxeles.
     */
    public int height() {
        int h = 0;
        for(Object o : items){
            if(o instanceof Cup) h += ((Cup)o).getHeight();
            if(o instanceof Lid) h += ((Lid)o).getHeight();
        }
        return h;
    }

    /**
     * Retorna un arreglo con los tamaños de las tazas que tienen su tapa puesta.
     * Una taza se considera tapada si su tapa está inmediatamente encima de ella.
     * * @return Arreglo de enteros con los tamaños.
     */
    public int[] lidedCups() {
        ArrayList<Integer> lided = new ArrayList<>();
        
        // Recorremos buscando pares consecutivos: Cup en i, Lid en i+1
        for(int i = 0; i < items.size() - 1; i++){
            Object current = items.get(i);
            Object next = items.get(i+1);
            
            if(current instanceof Cup && next instanceof Lid){
                // Verificar si corresponden al mismo tamaño
                if(((Cup)current).getSize() == ((Lid)next).getSize()){
                    lided.add(((Cup)current).getSize());
                }
            }
        }
        
        // Convertir ArrayList a int[]
        int[] result = new int[lided.size()];
        for(int i=0; i<lided.size(); i++) result[i] = lided.get(i);
        return result;
    }

    /**
     * Genera una matriz representando los ítems en la torre.
     * Formato: {{"cup", "size"}, {"lid", "size"}}
     * * @return Matriz de Strings.
     */
    public String[][] stackingitems() {
        String[][] result = new String[items.size()][2];
        for(int i=0; i<items.size(); i++){
            Object o = items.get(i);
            if(o instanceof Cup) {
                result[i][0] = "cup";
                result[i][1] = String.valueOf(((Cup)o).getSize());
            } else {
                result[i][0] = "lid";
                result[i][1] = String.valueOf(((Lid)o).getSize());
            }
        }
        return result;
    }

    /**
     * Hace visible la torre en el canvas.
     * Verifica primero si la altura total excede el máximo permitido.
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
     * Oculta la torre del canvas.
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
    public void exit(){
        System.exit(0);
    }
    
    /**
     * Retorna el estado de la última operación.
     * @return true si fue exitosa, false si hubo error.
     */
    public boolean ok(){
        return ok;
    }
    
    /**
     * Redibuja todos los elementos en el Canvas en su posición actual.
     * Calcula la posición Y apilando los objetos desde el suelo hacia arriba.
     */
    private void refreshView() {
        if(!isVisible) return;
        
        int currentY = 300; // Suelo del Canvas
        int centerX = 150;  // Centro del Canvas
        
        for (Object item : items) {
            if (item instanceof Cup) {
                Cup c = (Cup) item;
                // Restar altura antes de dibujar para apilar hacia arriba
                currentY -= c.getHeight();
                c.setPosition(centerX, currentY); 
                
            } else if (item instanceof Lid) {
                Lid l = (Lid) item;
                currentY -= l.getHeight();
                l.setPosition(centerX, currentY);
            }
        }
    }

    /**
     * Verifica si ya existe un ítem del tamaño y tipo dados.
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
    
    /**
     * Helper para obtener el tamaño de cualquier objeto (Cup o Lid).
     */
    private int getSize(Object o){
        if(o instanceof Cup) return ((Cup)o).getSize();
        if(o instanceof Lid) return ((Lid)o).getSize();
        return 0;
    }
    
    /**
     * Obtiene el color correspondiente para un tamaño dado.
     */
    private String getColorForSize(int i){
        int index = (i > 0) ? (i - 1) : 0;
        return colors[index % colors.length];
    }
    
    /**
     * Muestra un mensaje emergente si la torre es visible.
     */
    private void showMessage(String msg){
        if(isVisible){
            JOptionPane.showMessageDialog(null, msg);
        }
    }
}