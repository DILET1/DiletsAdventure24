public class InteractableObject extends WorldObject{
    protected String message;
    public InteractableObject(int lowx, int lowy, int hix, int hiy, String message) {
        super(lowx, lowy, hix, hiy);
        this.message = message;
    }
    public String msg(){
        return this.message;
    }


}
