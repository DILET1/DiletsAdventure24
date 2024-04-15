import java.util.ArrayList;

public class PlaceEvent extends Event{
    int toAdd;
    int curZone;
    ArrayList<Zone> Zonelist;
    ArrayList<WorldObject> objs;
    public PlaceEvent (Player dil, int curZone, int toAdd, ArrayList<Zone> zoneList, ArrayList<WorldObject> objs) {
        super("this isn't even used", true, dil);
        this.toAdd = toAdd;
        this.curZone = curZone;
        this.Zonelist = zoneList;
        this.objs = objs;
    }
    public String message(){
        this.Zonelist.get(curZone).addObj(objs.get(toAdd));
        return "Added object.";
    }
}
