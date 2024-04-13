public class NPC extends InteractableObject{
    DialogueOption firstD;
    String name;
    public NPC(int lowx, int lowy, int hix, int hiy, String message, DialogueOption firstD, String name) {
        super(lowx, lowy, hix, hiy, message);
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
