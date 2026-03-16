import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JOptionPane;

/**
 * La clase Tower es el controlador principal del simulador.
 * @authors Juan Nicolás Álvarez, Leonardo Rojas
 */
public class Tower {
    private int width;
    private int maxHeight;
    private boolean isVisible;
    private ArrayList<TowerItem> items;
    private boolean ok; 
    private String[] colors = {"red", "blue", "yellow", "green", "magenta"};

    public Tower(int width, int maxHeight) {
        this.width = width;
        this.maxHeight = maxHeight;
        this.items = new ArrayList<>();
        this.isVisible = false;
        this.ok = true;
    }

    public Tower(int cups) {
        this(500, 1000); 
        this.create(cups);
    }

    public void create(int number) {
        items.clear();
        for (int i = 1; i <= number; i++) pushCup(i);
        ok = true;
    }

    public void swap(int i, int j) {
        if (i >= 0 && i < items.size() && j >= 0 && j < items.size()) {
            Collections.swap(items, i, j);
            ok = true;
            refreshView();
        } else {
            ok = false;
        }
    }

    public void cover() {
        for (int i = 0; i < items.size(); i++) {
            TowerItem current = items.get(i);
            if (current.getTypeString().equals("cup")) {
                for (int j = 0; j < items.size(); j++) {
                    TowerItem potentialLid = items.get(j);
                    if (potentialLid.canCover(current)) { 
                        if (j != i + 1) {
                            TowerItem lid = items.remove(j);
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

    public String[][] swapToReduce() {
        int currentHeight = height();
        for (int i = 0; i < items.size() - 1; i++) {
            for (int j = i + 1; j < items.size(); j++) {
                Collections.swap(items, i, j);
                int newHeight = height();
                Collections.swap(items, i, j);
                if (newHeight < currentHeight) {
                    String[][] result = new String[2][2];
                    result[0] = new String[]{items.get(i).getTypeString(), String.valueOf(items.get(i).getSize())};
                    result[1] = new String[]{items.get(j).getTypeString(), String.valueOf(items.get(j).getSize())};
                    ok = true;
                    return result;
                }
            }
        }
        ok = false;
        return new String[0][0]; 
    }

    public void pushCup(int i) {
        if (existsItem(i, "cup")) { ok = false; return; }
        items.add(new Cup(i, getColorForSize(i)));
        ok = true;
        refreshView();
    }

    public void pushLid(int i) {
        if (existsItem(i, "lid")) { ok = false; return; }
        items.add(new Lid(i, getColorForSize(i)));
        ok = true;
        refreshView();
    }

    public void popCup() {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (items.get(i).getTypeString().equals("cup")) {
                items.get(i).setVisible(false);
                items.remove(i);
                ok = true;
                refreshView();
                return;
            }
        }
        ok = false;
    }

    public void popLid() {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (items.get(i).getTypeString().equals("lid")) {
                items.get(i).setVisible(false);
                items.remove(i);
                ok = true;
                refreshView();
                return;
            }
        }
        ok = false;
    }
    
    public void removeCup(int size) {
        boolean removed = items.removeIf(item -> item.getTypeString().equals("cup") && item.getSize() == size);
        if(removed) { refreshView(); ok = true; } else { ok = false; }
    }
    
    public void removeLid(int size) {
        boolean removed = items.removeIf(item -> item.getTypeString().equals("lid") && item.getSize() == size);
        if(removed) { refreshView(); ok = true; } else { ok = false; }
    }

    public void orderTower() {
        items.sort((o1, o2) -> Integer.compare(o2.getSize(), o1.getSize()));
        for(int i = 0; i < items.size() - 1; i++){
            TowerItem current = items.get(i);
            TowerItem next = items.get(i+1);
            if(current.getSize() == next.getSize() && current.getTypeString().equals("lid") && next.getTypeString().equals("cup")){
                Collections.swap(items, i, i+1);
            }
        }
        ok = true;
        refreshView();
    }

    public void reverseTower() {
        Collections.reverse(items);
        ok = true;
        refreshView();
    }

    public int height() {
        int h = 0;
        TowerItem previous = null;
        for(TowerItem item : items){
            h += item.getAddedHeightWhenPlacedOn(previous);
            previous = item;
        }
        return h;
    }

    public int[] lidedCups() {
        ArrayList<Integer> lided = new ArrayList<>();
        for(int i = 0; i < items.size() - 1; i++){
            TowerItem current = items.get(i);
            TowerItem next = items.get(i+1);
            if (next.canCover(current)) {
                lided.add(current.getSize());
            }
        }
        int[] result = new int[lided.size()];
        for(int i=0; i<lided.size(); i++) result[i] = lided.get(i);
        return result;
    }

    public String[][] stackingitems() {
        String[][] result = new String[items.size()][2];
        for(int i=0; i<items.size(); i++){
            result[i] = new String[]{items.get(i).getTypeString(), String.valueOf(items.get(i).getSize())};
        }
        return result;
    }

    public void makeVisible() {
        if (height() > maxHeight) {
            isVisible = false;
            showMessage("La torre excede la altura máxima permitida.");
        } else {
            isVisible = true;
            refreshView();
        }
    }

    public void makeInvisible() {
        isVisible = false;
        for(TowerItem item : items){
            item.setVisible(false);
        }
    }
    
    public void exit() { System.exit(0); }
    public boolean ok() { return ok; }
    
    private void refreshView() {
        if(!isVisible) return;
        int currentY = 300; 
        int centerX = 150; 
        TowerItem previous = null;
        
        for (int i = 0; i < items.size(); i++) {
            TowerItem item = items.get(i);
            boolean isCovered = (i + 1 < items.size() && items.get(i+1).canCover(item));
            item.setCovered(isCovered);
            
            int addedHeight = item.getAddedHeightWhenPlacedOn(previous);
            currentY -= addedHeight;
            item.setPosition(centerX, currentY); 
            previous = item;
        }
    }

    private boolean existsItem(int size, String typeString) {
        for (TowerItem item : items) {
            if (item.getTypeString().equals(typeString) && item.getSize() == size) return true;
        }
        return false;
    }
    
    private String getColorForSize(int i){
        return colors[(i > 0 ? i - 1 : 0) % colors.length];
    }
    
    private void showMessage(String msg){
        if(isVisible) JOptionPane.showMessageDialog(null, msg);
    }
}