import java.util.ArrayList;

public class moveInteractable extends Event{
    int tm;
    int zone;
    int x;
    int y;
    int cx;
    int cy;
    int speed;
    boolean used;
    ArrayList<Zone> zones;
    public moveInteractable(Player dil, int curZone, int tm, int x, int y, int speed, int questID, int questStep, ArrayList<Zone> zones) {
        super("this isn't used", true, dil, questID, questStep);
        this.zone = curZone;
        this.zones = zones;
        this.tm = tm;
        this.x = x;
        this.y = y;
        this.speed = speed;
        used = false;
        cx = x;
        cy = y;
    }
    public String message(){
        if(!used){
            if (cx < 0) {
                zones.get(zone).imoveX(zones.get(zone).getZoneInteractableObjects().get(tm), Math.max(cx, -1 * speed));
                cx -= (Math.max(cx, -1 * speed));
            }
            if(cx > 0){
                zones.get(zone).imoveX(zones.get(zone).getZoneInteractableObjects().get(tm), Math.min(cx, speed));
                cx-=(Math.min(cx, speed));
            }
            if (cy < 0) {
                zones.get(zone).imoveY(zones.get(zone).getZoneInteractableObjects().get(tm), Math.max(cy, -1 * speed));
                cy -= (Math.max(cy, -1 * speed));
            }
            if(cy > 0){
                zones.get(zone).imoveY(zones.get(zone).getZoneInteractableObjects().get(tm), Math.min(cy, speed));
                cy-=(Math.min(cy, speed));
            }
            if(cx == cy && cx == 0){
                used = true;
            }
        }
        return "moved object";
    }
    public void reset(){
        used = false; cx = x;
        cy = y;
    }
    public boolean done(){
        return (cx == cy) && (cx == 0);
    }
}