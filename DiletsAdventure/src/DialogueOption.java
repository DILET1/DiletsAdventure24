import java.awt.*;
import java.util.ArrayList;

public class DialogueOption {
    private ArrayList<Integer> adj;
    private Event trigger;
    private String label;
    public DialogueOption(ArrayList<Integer> adj, Event e, String label){
        this.adj = adj;
        this.trigger = e;
        this.label = label;
    }
    public ArrayList<Integer> getAdj(){
        return this.adj;
    }
    public String returnLabel(){
        return this.label;
    }
    public String use(){
        if(!this.trigger.isSilent()){
            return this.trigger.message();
        }
        else{
            return "Silent Event";
        }
    }

}
