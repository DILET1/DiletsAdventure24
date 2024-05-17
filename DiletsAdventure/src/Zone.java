import java.util.ArrayList;
public class Zone {
    int n, e, s, w, nx, ny, ex, ey, sx, sy, wx,wy;
    private ArrayList<WorldObject> zoneWorldObjects = new ArrayList<>();
    private ArrayList<InteractableObject> zoneInteractableObjects = new ArrayList<>();
    private ArrayList<InteractableObject> allZoneInteractableObject = new ArrayList<>();
    private ArrayList<NPC> zoneNPCs = new ArrayList<>();
    private ArrayList<NPC> allZoneNPCs = new ArrayList<>();
    private ArrayList<Chest> zoneChests = new ArrayList<>();
    private ArrayList<Chest> allZoneChests = new ArrayList<>();
    private ArrayList<Coordinate> zoneWorldObjectCoords = new ArrayList<>();
    private ArrayList<Coordinate> zoneInteractableObjectCoords = new ArrayList<>();
    private ArrayList<Coordinate> zoneNPCCoords = new ArrayList<>();
    private ArrayList<Coordinate> zoneChestCoords = new ArrayList<>();
    private ArrayList<Event> zoneEvents = new ArrayList<>();
    private ArrayList<ArrayList<DialogueOption>> zoneDialogue = new ArrayList<>();
    private ArrayList<Cutscene> zoneCutscenes = new ArrayList<>();

    public Coordinate getZoneWorldObjectCoords(int a){
        return zoneWorldObjectCoords.get(a);
    }
    public Coordinate getZoneInteractableObjectCoords(int a){
        return zoneInteractableObjectCoords.get(a);
    }
    public Coordinate getZoneNPCCoords(int a){
        return zoneNPCCoords.get(a);
    }
    public Coordinate getZoneChestCoords(int a){
        return zoneChestCoords.get(a);
    }

    public ArrayList<Coordinate> getZoneWorldObjectCoords(){
        return zoneWorldObjectCoords;
    }
    public ArrayList<Coordinate> getZoneInteractableObjectCoords(){
        return zoneInteractableObjectCoords;
    }
    public ArrayList<Coordinate> getZoneNPCCoords(){
        return zoneNPCCoords;
    }
    public ArrayList<Coordinate> getZoneChestCoords(){
        return zoneChestCoords;
    }

    public void wmoveX(WorldObject w, int amt){
        zoneWorldObjectCoords.get(zoneWorldObjects.indexOf(w)).addX(amt);
    }
    public void wmoveY(WorldObject w, int amt){
        zoneWorldObjectCoords.get(zoneWorldObjects.indexOf(w)).addY(amt);
    }
    public void imoveX(InteractableObject w, int amt){
        zoneInteractableObjectCoords.get(zoneInteractableObjects.indexOf(w)).addX(amt);
    }
    public void imoveY(InteractableObject w, int amt){
        zoneInteractableObjectCoords.get(zoneInteractableObjects.indexOf(w)).addY(amt);
    }
    public void nmoveX(NPC w, int amt){
        zoneNPCCoords.get(zoneNPCs.indexOf(w)).addX(amt);
    }
    public void nmoveY(NPC w, int amt){
        zoneNPCCoords.get(zoneNPCs.indexOf(w)).addY(amt);
    }
    //for add, check if the coordinate already exists, then it works fine.
    public void loadInteractable(InteractableObject a){
        allZoneInteractableObject.add(a);
    }
    public void loadNPC(NPC a){
        allZoneNPCs.add(a);
    }
    public void loadChest(Chest a){
        allZoneChests.add(a);

    }
    public void addInteractable(int a, int x, int y){
        if(!zoneInteractableObjects.contains(allZoneInteractableObject.get(a))){
            zoneInteractableObjects.add(allZoneInteractableObject.get(a));
            zoneInteractableObjectCoords.add(new Coordinate(x,y));
        }
        else{
            for(int i = 0; i < zoneInteractableObjects.size(); i++){
                if(zoneInteractableObjects.get(i) == allZoneInteractableObject.get(a) && zoneInteractableObjectCoords.get(i).getX() == x && zoneInteractableObjectCoords.get(i).getY() == y){
                    return;
                }
            }
            zoneInteractableObjects.add(allZoneInteractableObject.get(a));
            zoneInteractableObjectCoords.add(new Coordinate(x,y));
        }

    }
    public void addNPCs(int a, int x, int y){
        if(!zoneNPCs.contains(a)){
            zoneNPCs.add(allZoneNPCs.get(a));
            zoneNPCCoords.add(new Coordinate(x,y));
        }
        else{
            for(int i = 0; i < zoneNPCs.size(); i++){
                if(zoneNPCs.get(i) == allZoneNPCs.get(a) && zoneNPCCoords.get(i).getX() == x && zoneNPCCoords.get(i).getY() == y){
                    return;
                }
            }
            zoneNPCs.add(allZoneNPCs.get(a));
            zoneNPCCoords.add(new Coordinate(x,y));
        }
    }
    public void addChest(int a, int x, int y){
        if(!zoneChests.contains(a)){
            zoneChests.add(allZoneChests.get(a));
            zoneChestCoords.add(new Coordinate(x,y));
        }
        else{
            for(int i = 0; i < zoneChests.size(); i++){
                if(zoneChests.get(i) == allZoneChests.get(a) && zoneChestCoords.get(i).getX() == x && zoneChestCoords.get(i).getY() == y){
                    return;
                }
            }
            zoneChests.add(allZoneChests.get(a));
            zoneChestCoords.add(new Coordinate(x,y));
        }
    }
    public void addWorldObject(WorldObject a, int x, int y){
        if(!zoneWorldObjects.contains(a)){
            zoneWorldObjects.add(a);
            zoneWorldObjectCoords.add(new Coordinate(x, y));
        }
        else{
            for(int i = 0; i < zoneWorldObjects.size(); i++){
                if(zoneWorldObjects.get(i) == a && zoneWorldObjectCoords.get(i).getX() == x && zoneWorldObjectCoords.get(i).getY() == y){
                    return;
                }
            }
            zoneWorldObjects.add(a);
            zoneWorldObjectCoords.add(new Coordinate(x,y));
        }
    }
    public int getN(){
        return this.n;
    }
    public int getS(){
        return this.s;
    }
    public int getE(){
        return this.e;
    }
    public int getW(){
        return this.w;
    }

    public ArrayList<WorldObject> getZoneWorldObjects(){
        return zoneWorldObjects;
    }
    public ArrayList<InteractableObject> getZoneInteractableObjects(){return zoneInteractableObjects;}
    public ArrayList<NPC> getNPCs(){
        return zoneNPCs;
    }
    public ArrayList<Chest> getZoneChests(){
        return zoneChests;
    }

    public void removeWorldObject(WorldObject w, int x, int y){ //safe remove, when multiple of a same object are in the same zone. Remove the one with matching coordinates.
        for(int i = 0; i< zoneWorldObjects.size(); i++){
            if(zoneWorldObjects.get(i) == w && zoneWorldObjectCoords.get(i).getX() == x && zoneWorldObjectCoords.get(i).getY() == y){
                zoneWorldObjects.remove(w);
                zoneWorldObjectCoords.remove(i);
                return;
            }
        }
    }
    public void removeWorldObject(WorldObject w){ //unsafe remove. Removes first object that is the same type. Useful when the coordinate of a unique object is unknown.
        int ctr= 0;
        for(WorldObject a : zoneWorldObjects){
            if(a == w){
                zoneWorldObjects.remove(a);
                zoneWorldObjectCoords.remove(ctr);
                return;
            }
            ctr++;
        }
    }

    public void removeInteractable(InteractableObject w, int x, int y){ //safe remove, when multiple of a same object are in the same zone. Remove the one with matching coordinates.
        for(int i = 0; i< zoneInteractableObjects.size(); i++){
            if(zoneInteractableObjects.get(i) == w && zoneInteractableObjectCoords.get(i).getX() == x && zoneInteractableObjectCoords.get(i).getY() == y){
                zoneInteractableObjects.remove(w);
                zoneInteractableObjectCoords.remove(i);
                return;
            }
        }
    }
    public void removeInteractable(WorldObject w){//unsafe remove. Removes first object that is the same type. Useful when the coordinate of a unique object is unknown.
        int ctr = 0;
        for(InteractableObject a : zoneInteractableObjects){
            if(a == w){
                zoneInteractableObjects.remove(a);
                zoneInteractableObjectCoords.remove(ctr);
                return;
            }
            ctr++;
        }
    }

    public void removeNPC(NPC w, int x, int y){ //safe remove, when multiple of a same object are in the same zone. Remove the one with matching coordinates.
        for(int i = 0; i< zoneNPCs.size(); i++){
            if(zoneNPCs.get(i) == w && zoneNPCCoords.get(i).getX() == x && zoneNPCCoords.get(i).getY() == y){
                zoneNPCs.remove(w);
                zoneNPCCoords.remove(i);
                return;
            }
        }
    }
    public void removeNPC(NPC w){ //unsafe remove. Removes first object that is the same type. Useful when the coordinate of a unique object is unknown.
        int ctr = 0;
        for(NPC a : zoneNPCs){
            if(a == w){
                zoneNPCs.remove(a);
                zoneNPCCoords.remove(ctr);
                return;
            }
            ctr++;
        }
    }

    public void removeChest(Chest w, int x, int y){ //safe remove, when multiple of a same object are in the same zone. Remove the one with matching coordinates.
        for(int i = 0; i< zoneChests.size(); i++){
            if(zoneChests.get(i) == w && zoneChestCoords.get(i).getX() == x && zoneChestCoords.get(i).getY() == y){
                zoneChests.remove(w);
                zoneChestCoords.remove(i);
                return;
            }
        }
    }
    public void removeChest(Chest w){ //unsafe remove. Removes first object that is the same type. Useful when the coordinate of a unique object is unknown.
        int ctr = 0;
        for(Chest a : zoneChests){
            if(a == w){
                zoneChests.remove(a);
                zoneChestCoords.remove(ctr);
                return;
            }
            ctr++;

        }
    }

    public void addEvent(Event e){
        this.zoneEvents.add(e);
    }
    public Event getEvent(int a){
        return this.zoneEvents.get(a);
    }

    public void addDialogueOption(ArrayList<DialogueOption> d){
        this.zoneDialogue.add(d);
    }
    public DialogueOption getDialogue(int d, int NPCID){
        return this.zoneDialogue.get(NPCID).get(d);
    }

    public void addCutscene(Cutscene c){
        this.zoneCutscenes.add(c);
    }

    public Cutscene getCutscene(int a){
        return this.zoneCutscenes.get(a);
    }

    public Coordinate enterNorth(){
        return new Coordinate(nx, ny);
    }
    public Coordinate enterSouth(){
        return new Coordinate(sx, sy);
    }
    public Coordinate enterEast(){
        return new Coordinate(ex, ey);
    }
    public Coordinate enterWest(){
        return new Coordinate(wx, wy);
    }
    public Zone(int n, int e, int s, int w, int nx, int ny, int ex, int ey, int sx, int sy, int wx, int wy){
        this.n = n;
        this.s = s;
        this.w = w;
        this.e = e;
        this.nx = nx;
        this.ny = ny;
        this.ex = ex;
        this.ey = ey;
        this.sx = sx;
        this.sy = sy;
        this.wx = wx;
        this.wy = wy;
    }
}
