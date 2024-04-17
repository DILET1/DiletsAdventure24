import java.util.ArrayList;

public class moveObject extends Event{
    int tm;
    Zone zone;
    int x;
    int y;
    int cx;
    int cy;
    int speed;
    boolean used;
    ArrayList<WorldObject> objs;
    public moveObject(Player dil, Zone curZone, int tm, int x, int y, int speed, ArrayList<WorldObject> objs) {
        super("this isn't used", true, dil);
        this.zone = curZone;
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
                zone.wmoveX(objs.get(tm), Math.max(cx, -1 * speed));
                cx -= (Math.max(cx, -1 * speed));
            }
            if(cx > 0){
                zone.wmoveX(objs.get(tm), Math.min(cx, speed));
                cx-=(Math.min(cx, speed));
            }
            if (cy < 0) {
                zone.wmoveY(objs.get(tm), Math.max(cy, -1 * speed));
                cy -= (Math.max(cy, -1 * speed));
            }
            if(cy > 0){
                zone.wmoveY(objs.get(tm), Math.min(cy, speed));
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
}