public class NPC extends InteractableObject{
    DialogueOption firstD;
    String name;
    public NPC(int lowx, int lowy, int hix, int hiy, int eventInd, DialogueOption firstD, String name) {
        super(lowx, lowy, hix, hiy, eventInd);
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
