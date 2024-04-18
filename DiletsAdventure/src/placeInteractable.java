import java.util.ArrayList;

public class placeInteractable extends Event{
    int toAdd;
    int curZone;
    ArrayList<Zone> Zonelist;
    ArrayList<InteractableObject> its;
    int x;
    int y;
    public placeInteractable (Player dil, int curZone, int toAdd, int x, int y, int questID, int questStep,ArrayList<Zone> zoneList, ArrayList<InteractableObject> its) {
        super("this isn't even used", true, dil, questID, questStep);
        this.toAdd = toAdd;
        this.curZone = curZone;
        this.Zonelist = zoneList;
        this.its = its;
        this.x = x;
        this.y = y;
    }
    public String message(){
        this.Zonelist.get(curZone).addInteractable(its.get(toAdd),x,y);
        return "Added Interactable.";
    }
}
