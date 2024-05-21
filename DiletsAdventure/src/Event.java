import java.util.ArrayList;
public class Event {
    private String message;
    private boolean silent;
    protected int questId;
    protected int questStep;
    protected Player dil;
    public Event(String message, boolean silent, Player dil, int questId, int questStep){
        this.message = message;
        this.silent = silent;
        this.dil = dil;
        this.questId = questId;
        this.questStep = questStep;
    }
    public String message(){
        return this.message;
    }
    public boolean isSilent(){
        return silent;
    }
    public void reset(){
        return;
    }
    public boolean isCutscene(){
        return false;
    }

    public ArrayList<Integer> getDelays() {
        return new ArrayList<Integer>();
    }
    public ArrayList<Event> getSeq(){
        return new ArrayList<Event>();
    }
    public boolean done(){
        return true;
    }
    public int returnQuestID(){
        return questId;
    }
    public int returnQuestStep(){
        return questStep;
    }
    public void setMessage(String nmessage){
        message = nmessage;
    }
}
