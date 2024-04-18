public class TakeEvent extends Event{
    private Item ta;
    public TakeEvent(String message, boolean silent, Player dil, Item ta, int questID, int questStep) {
        super(message, silent, dil, questID, questStep);
        this.ta = ta;
    }
    public String message(){
        dil.takeItem(ta);
        return super.message();
    }
}
