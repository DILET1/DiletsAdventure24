import java.util.ArrayList;
public class Zone {
    private ArrayList<WorldObject> obstacles = new ArrayList<>();
    public String id;
    public Zone(String id){
        this.id = id;
    }
    public void addObj(WorldObject a){
        obstacles.add(a);
    }
    public ArrayList<WorldObject> getObstacles(){
        return obstacles;
    }
}
