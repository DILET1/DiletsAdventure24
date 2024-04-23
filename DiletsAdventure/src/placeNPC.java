import java.util.ArrayList;

public class placeNPC extends Event{
    int toAdd;
    int curZone;
    ArrayList<Zone> Zonelist;
    int x;
    int y;
    public placeNPC(Player dil, int curZone, int toAdd, int x, int y, int questID, int questStep, ArrayList<Zone> zoneList) {
        super("this isn't even used", true, dil, questID, questStep);
        this.toAdd = toAdd;
        this.curZone = curZone;
        this.Zonelist = zoneList;
        this.x = x;
        this.y = y;
    }
    public String message(){
        this.Zonelist.get(curZone).addNPCs(toAdd, x, y);
        return "Added NPC.";
    }
}
