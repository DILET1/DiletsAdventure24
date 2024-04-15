import java.util.ArrayList;

public class placeInteractable extends Event{
    int toAdd;
    int curZone;
    ArrayList<Zone> Zonelist;
    ArrayList<WorldObject> objs;
    ArrayList<InteractableObject> its;
    public placeInteractable (Player dil, int curZone, int toAdd, ArrayList<Zone> zoneList, ArrayList<WorldObject> objs, ArrayList<InteractableObject> its) {
        super("this isn't even used", true, dil);
        this.toAdd = toAdd;
        this.curZone = curZone;
        this.Zonelist = zoneList;
        this.objs = objs;
        this.its = its;
    }
    public String message(){
        this.Zonelist.get(curZone).addObj(objs.get(toAdd));
        this.Zonelist.get(curZone).addInteractables(its.get(toAdd));
        return "Added Interactable.";
    }
}
