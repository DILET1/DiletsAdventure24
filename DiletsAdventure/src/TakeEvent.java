public class TakeEvent extends Event{
    private Item ta;
    public TakeEvent(String message, boolean silent, Player dil, Item ta) {
        super(message, silent, dil);
        this.ta = ta;
    }
    public String message(){
        dil.takeItem(ta);
        return super.message();
    }
}
