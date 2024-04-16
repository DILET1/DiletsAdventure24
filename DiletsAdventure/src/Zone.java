import java.util.ArrayList;
public class Zone {
    int n, e, s, w;
    private ArrayList<WorldObject> obstacles = new ArrayList<>();
    private ArrayList<InteractableObject> interactables = new ArrayList<>();
    private ArrayList<NPC> NPCS = new ArrayList<>();
    private ArrayList<Chest> chests = new ArrayList<>();
    public void addObj(WorldObject a){
        if(!obstacles.contains(a)){
            obstacles.add(a);
        }
    }
    public void addInteractables(InteractableObject a){
        if(!interactables.contains(a)){
            interactables.add(a);
        }

    }
    public void addNPCs(NPC a){
        if(!NPCS.contains(a)){
            NPCS.add(a);
        }
    }
    public void addChest(Chest a){
        chests.add(a);
    }
    public ArrayList<WorldObject> getObstacles(){
        return obstacles;
    }
    public ArrayList<InteractableObject> getInteractables(){return interactables;}
    public ArrayList<NPC> getNPCs(){
        return NPCS;
    }
    public ArrayList<Chest> getChests(){
        return chests;
    }
    public Zone(int n, int e, int s, int w){
        this.n = n;
        this.s = s;
        this.w = w;
        this.e = e;
    }
}
