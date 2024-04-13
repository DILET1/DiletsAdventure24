public class GiveEvent extends Event{
    Item ta;

    public GiveEvent(String message, boolean silent, Player dil, Item ta){
        super(message, silent, dil);
        this.ta = ta;
    }
    public String message(){
        dil.addItem(ta);
        return super.message();
    }
}
