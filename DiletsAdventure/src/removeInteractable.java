import java.util.ArrayList;

public class removeInteractable extends Event{
    int toRemove;
    int curZone;
    ArrayList<Zone> Zonelist;
    ArrayList<WorldObject> objs;
    ArrayList<InteractableObject> its;
    public removeInteractable(Player dil, int curZone, int toRemove, ArrayList<Zone> zoneList, ArrayList<WorldObject> objs, ArrayList<InteractableObject> its) {
        super("this isn't even used", true, dil);
        this.toRemove = toRemove;
        this.curZone = curZone;
        this.Zonelist = zoneList;
        this.objs = objs;
        this.its = its;
    }
    public String message(){
        this.Zonelist.get(curZone).getObstacles().remove(objs.get(toRemove));
        this.Zonelist.get(curZone).getInteractables().remove(its.get(toRemove));
        return "Removed object.";
    }
}
