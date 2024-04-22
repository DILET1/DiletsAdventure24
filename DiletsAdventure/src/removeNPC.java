import java.util.ArrayList;

public class removeNPC extends Event{
    int toRemove;
    int curZone;
    ArrayList<Zone> Zonelist;
    public removeNPC(Player dil, int curZone, int toRemove, int questID, int questStep, ArrayList<Zone> zoneList) {
        super("this isn't even used", true, dil, questID, questStep);
        this.toRemove = toRemove;
        this.curZone = curZone;
        this.Zonelist = zoneList;
    }
    public String message(){
        this.Zonelist.get(curZone).removeNPC(this.Zonelist.get(curZone).getNPCs().get(toRemove));
        return "Removed object.";
    }
}
