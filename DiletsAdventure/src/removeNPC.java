import java.util.ArrayList;

public class removeNPC extends Event{
    int toRemove;
    int curZone;
    ArrayList<Zone> Zonelist;
    ArrayList<NPC> its;
    public removeNPC(Player dil, int curZone, int toRemove, int questID, int questStep, ArrayList<Zone> zoneList, ArrayList<NPC> its) {
        super("this isn't even used", true, dil, questID, questStep);
        this.toRemove = toRemove;
        this.curZone = curZone;
        this.Zonelist = zoneList;
        this.its = its;
    }
    public String message(){
        this.Zonelist.get(curZone).removeNPC(its.get(toRemove));
        return "Removed object.";
    }
}
