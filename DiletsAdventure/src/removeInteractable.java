import java.util.ArrayList;

public class removeInteractable extends Event{
    int toRemove;
    int curZone;
    ArrayList<Zone> Zonelist;
    boolean used;
    int x;
    int y;
    public removeInteractable(Player dil, int curZone, int toRemove, int x, int y, int questID, int questStep, ArrayList<Zone> zoneList) {
        super("this isn't even used", true, dil, questID, questStep);
        this.toRemove = toRemove;
        this.curZone = curZone;
        this.Zonelist = zoneList;
        this.used = false;
        this.x = x;
        this.y = y;
    }
    public String message(){
        if(!used){
            if(this.x == this.y && this.x == -1){
                this.Zonelist.get(curZone).removeInteractable(this.Zonelist.get(curZone).getZoneInteractableObjects().get(toRemove));
            }
            else{
                this.Zonelist.get(curZone).removeInteractable(this.Zonelist.get(curZone).getZoneInteractableObjects().get(toRemove), x, y);
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
