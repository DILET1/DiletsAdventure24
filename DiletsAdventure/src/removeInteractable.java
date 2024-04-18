import java.util.ArrayList;

public class removeInteractable extends Event{
    int toRemove;
    int curZone;
    ArrayList<Zone> Zonelist;
    ArrayList<InteractableObject> its;
    boolean used;
    int x;
    int y;
    public removeInteractable(Player dil, int curZone, int toRemove, int x, int y, int questID, int questStep, ArrayList<Zone> zoneList, ArrayList<InteractableObject> its) {
        super("this isn't even used", true, dil, questID, questStep);
        this.toRemove = toRemove;
        this.curZone = curZone;
        this.Zonelist = zoneList;
        this.its = its;
        this.used = false;
        this.x = x;
        this.y = y;
    }
    public String message(){
        if(!used){
            if(this.x == this.y && this.x == -1){
                this.Zonelist.get(curZone).removeInteractable(its.get(toRemove));
            }
            else{
                this.Zonelist.get(curZone).removeInteractable(its.get(toRemove), x, y);
            }
            used = true;
            return "Removed object.";
        }
        return "Dupe protection";
    }
    public void reset(){
        used = false;
    }
}
