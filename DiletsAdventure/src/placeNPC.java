import java.util.ArrayList;

public class placeNPC extends Event{
    int toAdd;
    int curZone;
    ArrayList<Zone> Zonelist;
    ArrayList<WorldObject> objs;
    ArrayList<NPC> its;
    public placeNPC(Player dil, int curZone, int toAdd, ArrayList<Zone> zoneList, ArrayList<WorldObject> objs, ArrayList<NPC> its) {
        super("this isn't even used", true, dil);
        this.toAdd = toAdd;
        this.curZone = curZone;
        this.Zonelist = zoneList;
        this.objs = objs;
        this.its = its;
    }
    public String message(){
        this.Zonelist.get(curZone).addObj(objs.get(toAdd));
        this.Zonelist.get(curZone).addNPCs(its.get(toAdd));
        return "Added NPC.";
    }
}
