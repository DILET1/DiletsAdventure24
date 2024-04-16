import java.util.ArrayList;
public class Chest extends InteractableObject{
    private ArrayList<Item> contents;
    private Player Dilet;
    public Chest(int x, int y, ArrayList<Item> ta, Player Dilet) {
        super(x, y,0);
        this.contents = ta;
        this.Dilet = Dilet;
    }
    public int msg(){
        for(Item i : this.contents){
            Dilet.addItem(i);
        }
        this.contents.clear();
        return 0;
    }
    public ArrayList<Item> getContents(){
        return contents;
    }
    public void removeItem(Item i){
        this.contents.remove(i);
    }
}
