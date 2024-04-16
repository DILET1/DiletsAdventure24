public class InteractableObject extends WorldObject{
    protected int eventInd;
    public InteractableObject(int x, int y, int eventInd) {
        super(x,y);
        this.eventInd = eventInd;
    }
    public int msg(){
        return this.eventInd;
    }



}
