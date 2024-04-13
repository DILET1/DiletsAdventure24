import java.util.ArrayList;
public class Zone {
    private ArrayList<WorldObject> obstacles = new ArrayList<>();
    private ArrayList<InteractableObject> interactables = new ArrayList<>();
    private ArrayList<NPC> NPCS = new ArrayList<>();
    public String id;
    public Zone(String id){
        this.id = id;
    }
    public void addObj(WorldObject a){
        obstacles.add(a);
    }
    public void addInteractables(InteractableObject a){
        interactables.add(a);
    }
    public void addNPCs(NPC a){
        NPCS.add(a);
    }
    public ArrayList<WorldObject> getObstacles(){
        return obstacles;
    }
    public ArrayList<InteractableObject> getInteractables(){return interactables;}
    public ArrayList<NPC> getNPCs(){
        return NPCS;
    }
}
