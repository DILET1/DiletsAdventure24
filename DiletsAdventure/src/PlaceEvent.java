import java.util.ArrayList;

public class PlaceEvent extends Event{
    int toAdd;
    int curZone;
    ArrayList<Zone> Zonelist;
    ArrayList<WorldObject> objs;
    int x;
    int y;
    boolean used;
    public PlaceEvent (Player dil, int curZone, int toAdd, int x, int y, int questID, int questStep, ArrayList<Zone> zoneList, ArrayList<WorldObject> objs) {
        super("this isn't even used", true, dil, questID, questStep);
        this.toAdd = toAdd;
        this.curZone = curZone;
        this.Zonelist = zoneList;
        this.objs = objs;
        this.x = x;
        this.y = y;
        this.used = false;
    }
    public String message(){
        if(!used){
            used = true;
            this.Zonelist.get(curZone).addWorldObject(objs.get(toAdd), x, y);
            return "Added object.";
        }
        return "duplicate protection enabled for place";
    }
    public void reset(){
        used = false;
    }

}
