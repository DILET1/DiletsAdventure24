public class moveObject extends Event{
    WorldObject actor;
    int dy=0;
    int dx = 0;
    int speed;
    public moveObject(WorldObject actor, int speed, int dx, int dy, Player dil) {
        super("moved", true, dil);
        this.actor = actor;
        this.dy = dy;
        this.dx = dx;
        this.speed = speed;
    }
    public String message(){
        int px = dx;
        int py = dy;
        while(px != 0 || py != 0){
            if(px != 0){
                if(px < 0){
                    this.actor.moveX(Math.max(px, -1 * speed));
                }
                else{
                    this.actor.moveX(Math.min(px, speed));
                }
            }
            if(py != 0){
                if(py < 0){
                    this.actor.moveY(Math.max(py, -1 * speed));
                }
                else{
                    this.actor.moveY(Math.min(py, speed));
                }
            }
        }
        return "Finished moving";
    }
}
