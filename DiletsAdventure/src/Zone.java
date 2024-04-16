import java.util.ArrayList;
public class Zone {
    int n, e, s, w;
    private ArrayList<WorldObject> obstacles = new ArrayList<>();
    private ArrayList<InteractableObject> interactables = new ArrayList<>();
    private ArrayList<NPC> NPCS = new ArrayList<>();
    private ArrayList<Chest> chests = new ArrayList<>();
    private ArrayList<Coordinate> coords = new ArrayList<>();
    private ArrayList<Coordinate> icoords = new ArrayList<>();
    private ArrayList<Coordinate> ncoords = new ArrayList<>();
    private ArrayList<Coordinate> ccoords = new ArrayList<>();
    public void addObj(WorldObject a, int x, int y){
        if(!obstacles.contains(a)){
            obstacles.add(a);
            coords.add(new Coordinate(x, y));
        }
    }
    public Coordinate getCoord(int a){
        return coords.get(a);
    }
    public Coordinate getIcoord(int a){
        return icoords.get(a);
    }
    public Coordinate getnCoord(int a){
        return ncoords.get(a);
    }    public Coordinate getCcoord(int a){
        return ccoords.get(a);
    }

    public ArrayList<Coordinate> getCoords(){
        return coords;
    }
    public ArrayList<Coordinate> getIcoords(){
        return icoords;
    }
    public ArrayList<Coordinate> getNcoords(){
        return ncoords;
    }
    public ArrayList<Coordinate> getCcoords(){
        return ccoords;
    }
    public void wmoveX(WorldObject w, int amt){
        coords.get(obstacles.indexOf(w)).addX(amt);
    }
    public void wmoveY(WorldObject w, int amt){
        coords.get(obstacles.indexOf(w)).addY(amt);
    }
    //for add, check if the coordinate already exists, then it works fine.
    public void addInteractables(InteractableObject a, int x, int y){
        interactables.add(a);
        icoords.add(new Coordinate(x,y));
    }
    public void addNPCs(NPC a, int x, int y){
        NPCS.add(a);
        ncoords.add(new Coordinate(x,y));
    }
    public void addChest(Chest a, int x, int y){
        chests.add(a); ccoords.add(new Coordinate(x,y));
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
    public void removeObject(WorldObject w, int x, int y){ //safe remove, when multiple of a same object are in the same zone. Remove the one with matching coordinates.
        for(int i =0; i< obstacles.size(); i++){
            if(obstacles.get(i) == w && coords.get(i).getX() == x && coords.get(i).getY() == y){
                obstacles.remove(w);
                coords.remove(i);
                return;
            }
        }
    }
    public void removeObject(WorldObject w){ //unsafe remove. Removes first object that is the same type. Useful when the coordinate of a unique object is unknown.
        int ctr= 0;
        for(WorldObject a : obstacles){
            if(a == w){
                obstacles.remove(a);
                coords.remove(ctr);
                return;
            }
            ctr++;
        }
    }
    public void removeInteractable(InteractableObject w, int x, int y){ //safe remove, when multiple of a same object are in the same zone. Remove the one with matching coordinates.
        for(int i =0; i< interactables.size(); i++){
            if(interactables.get(i) == w && icoords.get(i).getX() == x && icoords.get(i).getY() == y){
                interactables.remove(w);
                icoords.remove(i);
                return;
            }
        }
    }
    public void removeInteractable(WorldObject w){//unsafe remove. Removes first object that is the same type. Useful when the coordinate of a unique object is unknown.
        int ctr = 0;
        for(InteractableObject a : interactables){
            if(a == w){
                interactables.remove(a);
                icoords.remove(ctr);
                return;
            }
            ctr++;
        }
    }
    public void removeNPC(NPC w, int x, int y){ //safe remove, when multiple of a same object are in the same zone. Remove the one with matching coordinates.
        for(int i =0; i< NPCS.size(); i++){
            if(NPCS.get(i) == w && ncoords.get(i).getX() == x && ncoords.get(i).getY() == y){
                NPCS.remove(w);
                ncoords.remove(i);
                return;
            }
        }
    }
    public void removeNPC(NPC w){ //unsafe remove. Removes first object that is the same type. Useful when the coordinate of a unique object is unknown.
        int ctr = 0;
        for(NPC a : NPCS){
            if(a == w){
                NPCS.remove(a);
                ncoords.remove(ctr);
                return;
            }
            ctr++;
        }
    }
    public void removeChest(Chest w, int x, int y){ //safe remove, when multiple of a same object are in the same zone. Remove the one with matching coordinates.
        for(int i =0; i< chests.size(); i++){
            if(chests.get(i) == w && ccoords.get(i).getX() == x && ccoords.get(i).getY() == y){
                chests.remove(w);
                ccoords.remove(i);
                return;
            }
        }
    }
    public void removeChest(Chest w){ //unsafe remove. Removes first object that is the same type. Useful when the coordinate of a unique object is unknown.
        int ctr = 0;
        for(Chest a : chests){
            if(a == w){
                chests.remove(a);
                ccoords.remove(ctr);
                return;
            }
            ctr++;

        }
    }
    public Zone(int n, int e, int s, int w){
        this.n = n;
        this.s = s;
        this.w = w;
        this.e = e;
    }
}
