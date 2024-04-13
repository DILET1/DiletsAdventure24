public class Event {
    private String message;
    private boolean silent;
    protected Player dil;
    public Event(String message, boolean silent, Player dil){
        this.message = message;
        this.silent = silent;
        this.dil = dil;
    }
    public String message(){
        return this.message;
    }
    public boolean isSilent(){
        return silent;
    }

}
