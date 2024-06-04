import java.util.ArrayList;

public class Attack {
    ArrayList<Integer> delays = new ArrayList<>(); //between salvoes in one arraylist of projectiles
    ArrayList<Integer> lengths = new ArrayList<>();//how long to launch each salvo for
    ArrayList<ArrayList<Projectile>> projectiles = new ArrayList<>();
    public void addStep(int delay, int length, ArrayList<Projectile> p){
        delays.add(delay);
        this.lengths.add(length);
        projectiles.add(p);
        System.out.println("Added step!");
    }
    public int getSize(){
        return projectiles.size();
    }
    public int getDelay(int i){
        return delays.get(i);
    }
    public ArrayList<Projectile> getStep(int i){
        return projectiles.get(i);
    }
    public int getLengths(int i){
        return lengths.get(i);
    }
}
