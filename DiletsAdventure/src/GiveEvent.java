public class GiveEvent extends Event{
    Item ta;

    public GiveEvent(String message, boolean silent, Player dil, Item ta, int questID, int questStep){
        super(message, silent, dil, questID, questStep);
        this.ta = ta;
    }
    public String message(){
        dil.addItem(ta);
        return super.message();
    }
}
