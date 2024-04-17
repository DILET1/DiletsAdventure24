import java.util.ArrayList;

public class RemoveEvent extends Event{
    int toRemove;
    int curZone;
    ArrayList<Zone> Zonelist;
    ArrayList<WorldObject> objs;
    int x;
    int y;
    boolean used;
    public RemoveEvent(Player dil, int curZone, int toRemove, int x, int y, ArrayList<Zone> zoneList, ArrayList<WorldObject> objs) {
        super("this isn't even used", true, dil);
        this.toRemove = toRemove;
        this.curZone = curZone;
        this.Zonelist = zoneList;
        this.objs = objs;
        this.used = false;
        this.x = x;
        this.y = y;
    }
    public String message(){
        if(!used){
            used = true;
            if(this.x == this.y && this.x == -1){
                this.Zonelist.get(curZone).removeObject(objs.get(toRemove));
            }
            else{
                this.Zonelist.get(curZone).removeObject(objs.get(toRemove), x, y);
            }
            return "Removed object.";
        }
        return "Duplicate protection for remove";
    }
    public void reset(){
        used = false;
    }
}
