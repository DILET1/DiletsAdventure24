import java.util.ArrayList;

public class placeInteractable extends Event{
    int toAdd;
    int curZone;
    ArrayList<Zone> Zonelist;
    ArrayList<WorldObject> objs;
    ArrayList<InteractableObject> its;
    int x;
    int y;
    public placeInteractable (Player dil, int curZone, int toAdd, int x, int y, ArrayList<Zone> zoneList, ArrayList<WorldObject> objs, ArrayList<InteractableObject> its) {
        super("this isn't even used", true, dil);
        this.toAdd = toAdd;
        this.curZone = curZone;
        this.Zonelist = zoneList;
        this.objs = objs;
        this.its = its;
        this.x = x;
        this.y = y;
    }
    public String message(){
        this.Zonelist.get(curZone).addInteractables(its.get(toAdd),x,y);
        return "Added Interactable.";
    }
}
