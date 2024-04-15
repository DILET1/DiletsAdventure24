import java.util.ArrayList;

public class RemoveEvent extends Event{
    int toRemove;
    int curZone;
    ArrayList<Zone> Zonelist;
    ArrayList<WorldObject> objs;
    public RemoveEvent(Player dil, int curZone, int toRemove, ArrayList<Zone> zoneList, ArrayList<WorldObject> objs) {
        super("this isn't even used", true, dil);
        this.toRemove = toRemove;
        this.curZone = curZone;
        this.Zonelist = zoneList;
        this.objs = objs;
    }
    public String message(){
        this.Zonelist.get(curZone).getObstacles().remove(objs.get(toRemove));
        return "Removed object.";
    }
}
