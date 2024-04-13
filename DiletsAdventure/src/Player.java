import java.util.ArrayList;
public class Player {
    private int x, y, radius, scalar;
    private ArrayList<Item> inventory = new ArrayList<>();
    public Player(int resScalar){
        this.x = 50 * resScalar;
        this.y = 50 * resScalar;
        this.radius = 15 * resScalar;
        this.scalar = resScalar;
    }
    void moveX(int amt){
        if(this.x - radius + amt > 0 && this.x + amt + radius< 640 * this.scalar){
            this.x+=amt;
        }
    }
    void moveY(int amt){
        if(this.y + amt - radius > 30 * this.scalar && this.y + amt + radius < 360 * this.scalar){
            this.y+=amt;
        }
    }
    int getX(){
        return this.x;
    }
    int getRadius(){
        return this.radius;
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
    void addItem(Item ta){
        inventory.add(ta);
    }
    void takeItem(Item tt){
        for(Item i : inventory){
            if(i == tt){
                inventory.remove(tt);
                return;
            }
        }
    }
}
