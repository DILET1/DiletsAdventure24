public class NPC extends InteractableObject{
    DialogueOption firstD;
    String name;
    int index;
    public NPC(int x, int y, int eventInd, DialogueOption firstD, String name, int index) {
        super(x,y, eventInd);
        this.firstD = firstD;
        this.name = name;
        this.index = index;
    }
    public DialogueOption talk(){
        return this.firstD;
    }
    public String getName(){
        return this.name;
    }
    public int getIndex(){
        return index;
    }

}
