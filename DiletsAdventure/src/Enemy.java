import java.util.ArrayList;
public class Enemy {
    int radius, health;
    int resScalar;
    int speed;
    int startMillis;
    boolean started = false;
    int atkctr = 0;
    ArrayList<Attack> atkpt = new ArrayList<>();
    public Enemy(int radius, int health, int resScalar, int speed){
        this.radius = radius;
        this.health = health;
        this.resScalar = resScalar;
        this.speed = speed;
    }
    public double getRadius(){
        return this.radius;
    }
    public void takeDamage(int dmg){
        this.health-=dmg;
    }
    public int getHealth() {
        return this.health;
    }
    public ArrayList<Projectile> getAtk(int i, int ct){
        if(!started){
            started = true;
            startMillis = ct;
            atkctr = 0;
        }
        if(ct - startMillis > atkpt.get(i).getDelay(atkctr)){
            startMillis = ct;
            atkctr++;
        }
        if(atkctr == atkpt.get(i).getSize()){
            return null;
        }
        return atkpt.get(i).getStep(atkctr);
    }
    public void addAtk(Attack a){
        atkpt.add(a);
    }
    public int getSpeed(){
        return this.speed;
    }
}