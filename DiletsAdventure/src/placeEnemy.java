import java.util.ArrayList;

public class placeEnemy extends Event{
    int id;
    ArrayList<Enemy> ge;
    ArrayList<Enemy> ce;
    boolean placed = false;
    String msg;
    ArrayList<Coordinate> ecoords;
    int x, y;
    public placeEnemy(String message, boolean silent, Player dil, int questId, int questStep, int id, ArrayList<Enemy> ge, ArrayList<Enemy> ce, ArrayList<Coordinate> ecoords, int x, int y){
        super(message, silent, dil, questId, questStep);
        this.id = id;
        this.ge = ge;
        this.ce = ce;
        this.x = x;
        this.y = y;
        this.ecoords = ecoords;
        msg = message;
    }
    public String message(){
        if(!placed){
            this.ce.add(this.ge.get(id));
            placed = true;
            this.ecoords.add(new Coordinate(x,y));
        }
        return msg;
    }
    public void reset(){
        placed = false;
    }
}
