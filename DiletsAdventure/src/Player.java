import java.util.ArrayList;
public class Player {
    private int x, y, radius, scalar;
    private ArrayList<Item> inventory = new ArrayList<>();
    public Player(int resScalar){
        this.x = 100 * resScalar;
        this.y = 100 * resScalar;
        this.radius = 15 * resScalar;
        this.scalar = resScalar;
    }
    public void moveX(int amt){
        if(this.x - radius + amt >= 0 && this.x + amt + radius<=640 * this.scalar){
            this.x+=amt;
        }
    }
    public void moveY(int amt){
        if(this.y + amt - radius >= 30 * this.scalar && this.y + amt + radius <= 360 * this.scalar){
            this.y+=amt;
        }
    }
    public int getX(){
        return this.x;
    }
    public int getRadius(){
        return this.radius;
    }
    public int getY(){
        return this.y;
    }
    public boolean canRight(WorldObject a, Coordinate c){
        return this.x + radius != c.getX() || this.y +radius <= c.getY() || this.y - radius >= c.getY()+a.getHeight();
    }
    public boolean canLeft(WorldObject a, Coordinate c){
        return this.x - radius != c.getX()+a.getLen() || this.y +radius <= c.getY() || this.y - radius >= c.getY()+a.getHeight();
    }
    public boolean canUp(WorldObject a, Coordinate c){
        return this.y - radius != c.getY()+a.getHeight() || this.x + radius <= c.getX() || this.x - radius >= c.getX()+a.getLen();
    }
    public boolean canDown(WorldObject a, Coordinate c){
        return this.y + radius != c.getY() || this.x + radius <= c.getX() || this.x - radius >= c.getX()+a.getLen();
    }
    public void addItem(Item ta){
        inventory.add(ta);
    }
    public void takeItem(Item tt){
        for(Item i : inventory){
            if(i == tt){
                inventory.remove(tt);
                return;
            }
        }
    }
    public ArrayList<Item> getInventory(){
        return inventory;
    }
}
