import java.util.ArrayList;

public class placeNPC extends Event{
    int toAdd;
    int curZone;
    ArrayList<Zone> Zonelist;
    ArrayList<WorldObject> objs;
    int x;
    int y;
    ArrayList<NPC> its;
    public placeNPC(Player dil, int curZone, int toAdd, int x, int y, ArrayList<Zone> zoneList, ArrayList<WorldObject> objs, ArrayList<NPC> its) {
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
        this.Zonelist.get(curZone).addNPCs(its.get(toAdd), x, y);
        return "Added NPC.";
    }
}
