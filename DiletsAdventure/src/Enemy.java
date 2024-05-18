import java.util.ArrayList;
public class Enemy {
    int radius, health;
    int resScalar;
    int speed;
    int startMillis;
    boolean started = false;
    int atkctr = 0;
    int sinceLast = 0;
    boolean attackOver = false;
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
    public boolean isDone(){
        return attackOver;
    }
    public ArrayList<Projectile> getAtk(int i, int ct){
        if(!started){
            started = true;
            attackOver = false;
            startMillis = ct;
            atkctr = 0;
        }
        if(atkctr == atkpt.get(i).getSize()){
            attackOver = true;
            started = false;
            return null;
        }
        if(ct - startMillis > atkpt.get(i).getDelay(atkctr)){
            startMillis = ct;
            atkctr++;
        }
        if(atkctr == atkpt.get(i).getSize()){
            attackOver = true;
            started = false;
            return null;
        }
        if(ct - sinceLast >= atkpt.get(i).getLengths(atkctr)){
            sinceLast = ct;
            return atkpt.get(i).getStep(atkctr);
        }
        return null;
    }
    public void addAtk(Attack a){
        atkpt.add(a);
    }
    public int getSpeed(){
        return this.speed;
    }
    public int getPatterns(){return this.atkpt.size();}
}