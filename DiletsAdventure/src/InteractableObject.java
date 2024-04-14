public class InteractableObject extends WorldObject{
    protected int eventInd;
    public InteractableObject(int lowx, int lowy, int hix, int hiy, int eventInd) {
        super(lowx, lowy, hix, hiy);
        this.eventInd = eventInd;
    }
    public int msg(){
        return this.eventInd;
    }


}
