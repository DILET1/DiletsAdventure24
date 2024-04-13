import java.util.ArrayList;
public class Chest extends InteractableObject{
    private ArrayList<Item> contents;
    private Player Dilet;
    public Chest(int lowx, int lowy, int hix, int hiy, ArrayList<Item> ta, Player Dilet) {
        super(lowx, lowy, hix, hiy, "You got: ");
        this.contents = ta;
        this.Dilet = Dilet;
        for(Item i : this.contents){
            this.message+=i.getName()+" ";
        }
        this.message+="from the chest.";
    }
    public String msg(){
        for(Item i : this.contents){
            Dilet.addItem(i);
        }
        this.contents.clear();
        System.out.println(message);
        return this.message;
    }
}
