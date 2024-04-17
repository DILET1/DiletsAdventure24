import java.util.ArrayList;
public class Zone {
    int n, e, s, w;
    private ArrayList<WorldObject> zoneWorldObjects = new ArrayList<>();
    private ArrayList<InteractableObject> zoneInteractableObjects = new ArrayList<>();
    private ArrayList<NPC> zoneNPCs = new ArrayList<>();
    private ArrayList<Chest> zoneChests = new ArrayList<>();
    private ArrayList<Coordinate> zoneWorldObjectCoords = new ArrayList<>();
    private ArrayList<Coordinate> zoneInteractableObjectCoords = new ArrayList<>();
    private ArrayList<Coordinate> zoneNPCCoords = new ArrayList<>();
    private ArrayList<Coordinate> zoneChestCoords = new ArrayList<>();

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
    //for add, check if the coordinate already exists, then it works fine.

    public void addInteractable(InteractableObject a, int x, int y){
        if(!zoneInteractableObjects.contains(a)){
            zoneInteractableObjects.add(a);
            zoneInteractableObjectCoords.add(new Coordinate(x,y));
        }
        else{
            for(int i = 0; i < zoneInteractableObjects.size(); i++){
                if(zoneInteractableObjects.get(i) == a && zoneInteractableObjectCoords.get(i).getX() == x && zoneInteractableObjectCoords.get(i).getY() == y){
                    return;
                }
            }
            zoneInteractableObjects.add(a);
            zoneInteractableObjectCoords.add(new Coordinate(x,y));
        }

    }
    public void addNPCs(NPC a, int x, int y){
        if(!zoneNPCs.contains(a)){
            zoneNPCs.add(a);
            zoneNPCCoords.add(new Coordinate(x,y));
        }
        else{
            for(int i = 0; i < zoneNPCs.size(); i++){
                if(zoneNPCs.get(i) == a && zoneNPCCoords.get(i).getX() == x && zoneNPCCoords.get(i).getY() == y){
                    return;
                }
            }
            zoneNPCs.add(a);
            zoneNPCCoords.add(new Coordinate(x,y));
        }
    }
    public void addChest(Chest a, int x, int y){
        if(!zoneChests.contains(a)){
            zoneChests.add(a);
            zoneChestCoords.add(new Coordinate(x,y));
        }
        else{
            for(int i = 0; i < zoneChests.size(); i++){
                if(zoneChests.get(i) == a && zoneChestCoords.get(i).getX() == x && zoneChestCoords.get(i).getY() == y){
                    return;
                }
            }
            zoneChests.add(a);
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
    public Zone(int n, int e, int s, int w){
        this.n = n;
        this.s = s;
        this.w = w;
        this.e = e;
    }
}
