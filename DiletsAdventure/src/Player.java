public class Player {
    private int x, y, radius;
    public Player(){
        this.x = 0;
        this.y = 0;
        this.radius = 15;
    }
    void moveX(int amt){
        this.x+=amt;
    }
    void moveY(int amt){
        this.y+=amt;
    }
    int getX(){
        return this.x;
    }
    int getY(){
        return this.y;
    }
    boolean canRight(WorldObject a){
        return this.x + radius != a.getLowx() || this.y +radius <= a.getLowy() || this.y - radius >= a.getHiy();
    }
    boolean canLeft(WorldObject a){
        return this.x - radius != a.getHix() || this.y +radius <= a.getLowy() || this.y - radius >= a.getHiy();
    }
    boolean canUp(WorldObject a){
        return this.y - radius != a.getHiy() || this.x + radius <= a.getLowx() || this.x - radius >= a.getHix();
    }
    boolean canDown(WorldObject a){
        return this.y + radius != a.getLowy() || this.x + radius <= a.getLowx() || this.x - radius >= a.getHix();
    }
}
