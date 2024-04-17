import java.util.ArrayList;

public class moveNPC extends Event{
    int tm;
    int zone;
    int x;
    int y;
    int cx;
    int cy;
    int speed;
    boolean used;
    ArrayList<NPC> objs;
    ArrayList<Zone> zones;
    public moveNPC(Player dil, int curZone, int tm, int x, int y, int speed, ArrayList<Zone> zones, ArrayList<NPC> objs) {
        super("this isn't used", true, dil);
        this.zone = curZone;
        this.zones = zones;
        this.tm = tm;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.objs = objs;
        used = false;
        cx = x;
        cy = y;
    }
    public String message(){
        if(!used){
            if (cx < 0) {
                zones.get(zone).nmoveX(objs.get(tm), Math.max(cx, -1 * speed));
                cx -= (Math.max(cx, -1 * speed));
            }
            if(cx > 0){
                zones.get(zone).nmoveX(objs.get(tm), Math.min(cx, speed));
                cx-=(Math.min(cx, speed));
            }
            if (cy < 0) {
                zones.get(zone).nmoveY(objs.get(tm), Math.max(cy, -1 * speed));
                cy -= (Math.max(cy, -1 * speed));
            }
            if(cy > 0){
                zones.get(zone).nmoveY(objs.get(tm), Math.min(cy, speed));
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