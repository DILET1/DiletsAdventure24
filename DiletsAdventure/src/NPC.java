public class NPC extends InteractableObject{
    DialogueOption firstD;
    String name;
    public NPC(int x, int y, int eventInd, DialogueOption firstD, String name) {
        super(x,y, eventInd);
        this.firstD = firstD;
        this.name = name;
    }
    public DialogueOption talk(){
        return this.firstD;
    }
    public String getName(){
        return this.name;
    }

}
