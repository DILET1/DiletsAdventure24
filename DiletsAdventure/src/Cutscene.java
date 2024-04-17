import java.util.ArrayList;

public class Cutscene extends Event{
    ArrayList<Event> seq = new ArrayList<>();
    ArrayList<Integer> delays = new ArrayList<>();
    public Cutscene(Player dil) {
        super("Playin a cutscene", true, dil);
    }
    public void addEvent(Event e, int delay){
        seq.add(e);
        delays.add(delay);
    }
    public ArrayList<Event> getSeq(){
        return seq;
    }
    public ArrayList<Integer> getDelays(){
        return delays;
    }
    public boolean isCutscene(){
        return true;
    }
}
