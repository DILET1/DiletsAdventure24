import processing.core.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.*;
public class Main extends PApplet {
    //all of our fun vars
    static ArrayList<Zone> globalZones = new ArrayList<>();
    ArrayList<Projectile> activeProjectiles = new ArrayList<>();
    ArrayList<Coordinate> projCoords = new ArrayList<>();
    ArrayList<Enemy> enemies = new ArrayList<>();
    ArrayList<Coordinate> ecoords = new ArrayList<>();
    ArrayList<Integer> curAttk = new ArrayList<>();
    static ArrayList<Item> globalItems = new ArrayList<>();
    static ArrayList<WorldObject> globalObjects = new ArrayList<>();
    static ArrayList<Quest> globalQuests = new ArrayList<>();
    static ArrayList<Quest> questLog = new ArrayList<>();
    public static int resScalar = 2;
    static Player Dilet = new Player(resScalar);
    static int curState = 1; //state -1 is intro cutscene. state 0 is menu. state 1 is in-game. state 2 is pause menu. state 3 is interacting with an object. state 4 is dialogue. state 5 is chest. state 6 is inventory. state 7 is cutscene, state 8 is inventory, 9 is combatmore to come
    static Zone curZone = null;
    static InteractableObject curInteractive = null;
    static NPC curNPC = null;
    static DialogueOption curDialogueOption = null;
    static Chest curChest = null;
    static int curCutscene;
    static int elapsedTime;
    int sinceLastProjectile = 0;
    static int newStart; //updated each time the cutscene state is entered
    static int cutSceneInd;//ditto above
    static boolean up, left, right, down;
    int health = 100;
    PImage questB, questT, pauseB, itemB, evB, butB;
    ArrayList<String> pastEvs = new ArrayList<>();
    PFont cons;
    public static void load(){
        loadBase();
        try{
            File manifest = new File("baseData/MANIFEST.txt");
            Scanner in = new Scanner(manifest);
            int numFiles = in.nextInt();
            for(int i = 0; i < numFiles; i++){
                loadZone("baseData/zone"+i+"/", i);
            }
        }
        catch(FileNotFoundException e){
            return;
        }
        curZone = globalZones.get(0);
    }
    public static void loadBase(){
        try{
            File obj = new File("baseData/GLOBAL/WO.txt");
            Scanner in = new Scanner(obj);
            int n = in.nextInt();
            for(int i = 0; i < n; i++){
                globalObjects.add(new WorldObject(in.nextInt() * resScalar, in.nextInt()* resScalar));
            }
            File quests = new File("baseData/GLOBAL/QUESTS.txt");
            in = new Scanner(quests);
            n = in.nextInt();
            for(int i = 0; i < n; i++){
                int steps = in.nextInt();
                Quest q = new Quest(in.nextLine());
                for(int j = 0; j < steps; j++){
                    q.addStep(in.nextLine());
                }
                globalQuests.add(q);
            }
            File items = new File("baseData/GLOBAL/ITEMS.txt");
            in = new Scanner(items);
            n = in.nextInt();
            String chaff = in.nextLine();
            for(int i = 0; i < n; i++){
                globalItems.add(new Item(in.nextLine(), in.nextLine()));
            }
        }
        catch(FileNotFoundException e){
            return;
        }
    }
    public static void loadZone(String directory, int ind){
        try{
            File adj = new File(directory+"NEIGHBORS.txt");
            Scanner in = new Scanner(adj);
            globalZones.add(new Zone(in.nextInt(),in.nextInt(),in.nextInt(),in.nextInt(), in.nextInt(),in.nextInt(),in.nextInt(),in.nextInt(),in.nextInt(),in.nextInt(),in.nextInt(),in.nextInt()));
        }
        catch(FileNotFoundException e){
            return;
        }
        loadEvents(directory, ind);
        loadInteractables(directory, ind);
        loadNPCs(directory, ind);
        loadCutscenes(directory, ind);
        loadChests(directory, ind);
        try{
            File adj = new File(directory+"LAYOUT.txt");
            Scanner in = new Scanner(adj);
            int wos = in.nextInt();
            for(int i = 0; i < wos; i++){
                int id = in.nextInt();
                int x = in.nextInt();
                int y = in.nextInt();
                globalZones.get(ind).addWorldObject(globalObjects.get(id), x * resScalar , y * resScalar);
            }
            int interactables = in.nextInt();
            for(int i = 0; i < interactables; i++){
                int id = in.nextInt();
                int x = in.nextInt();
                int y = in.nextInt();
                globalZones.get(ind).addInteractable(id, x * resScalar , y* resScalar);
            }
            int npcs = in.nextInt();
            for(int i = 0; i < npcs; i++){
                int id = in.nextInt();
                int x = in.nextInt();
                int y = in.nextInt();
                globalZones.get(ind).addNPCs(id, x* resScalar, y* resScalar);
            }
            int chests = in.nextInt();
            for(int i = 0; i < chests; i++){
                System.out.println("ADDING CHEST FR");
                int id = in.nextInt();
                int x = in.nextInt();
                int y = in.nextInt();
                globalZones.get(ind).addChest(id, x* resScalar, y* resScalar);
            }
        }
        catch(FileNotFoundException e){
            return;
        }
    }
    public static void loadEvents(String directory, int ind){
        try{
            File f = new File(directory + "EVENTS.txt");
            Scanner in = new Scanner(f);
            int n = in.nextInt();
            String chaff = in.nextLine();
            for(int i = 0; i < n; i++){
                int type = in.nextInt();
                int questID = in.nextInt();
                int questStep = in.nextInt();
                chaff = in.nextLine();
                String message = in.nextLine();
                int isSilent = in.nextInt();
                if(type == 1){
                    globalZones.get(ind).addEvent(new Event(message, isSilent == 1, Dilet, questID, questStep));
                    System.out.println(message+" "+isSilent+" "+questID+" "+questStep);
                }
                else if(type > 1 && type < 8){
                    int zone = in.nextInt();
                    int index = in.nextInt();
                    int x = in.nextInt();
                    int y = in.nextInt();
                    if(type == 2){
                        globalZones.get(ind).addEvent(new PlaceEvent(Dilet, zone, index, x, y, questID, questStep, globalZones, globalObjects));
                    }
                    if(type == 3){
                        globalZones.get(ind).addEvent(new placeInteractable(Dilet, zone, index, x, y, questID, questStep, globalZones));
                    }
                    if(type == 4){
                        globalZones.get(ind).addEvent(new placeNPC(Dilet, zone, index, x, y, questID, questStep, globalZones));
                    }
                    if(type == 5){
                        globalZones.get(ind).addEvent(new RemoveEvent(Dilet, zone, index, x, y, questID, questStep, globalZones, globalObjects));
                    }
                    if(type == 6){
                        globalZones.get(ind).addEvent(new removeInteractable(Dilet, zone, index, x, y, questID, questStep, globalZones));
                    }
                    if(type == 7){
                        globalZones.get(ind).addEvent(new removeNPC(Dilet, zone, index, questID, questStep, globalZones));
                    }
                    System.out.println(message+" "+isSilent+" "+questID+" "+questStep+" "+zone+" "+index+" "+x+" "+y);
                }
                else if(type > 7 && type < 11){
                    int zone = in.nextInt();
                    int index = in.nextInt();
                    int dx = in.nextInt();
                    int dy = in.nextInt();
                    int speed = in.nextInt();
                    if(type == 8){
                        globalZones.get(ind).addEvent(new moveObject(Dilet, zone, index, dx,dy,speed,-1,-1,globalZones,globalObjects));
                    }
                    if(type == 9){
                        globalZones.get(ind).addEvent(new moveInteractable(Dilet, zone, index, dx,dy,speed,-1,-1,globalZones));
                    }
                    if(type == 10){
                        globalZones.get(ind).addEvent(new moveNPC(Dilet, zone, index, dx,dy,speed,-1,-1,globalZones));
                    }
                    System.out.println(message+" "+isSilent+" "+questID+" "+questStep+" "+zone+" "+index+" "+dx+" "+dy+" "+speed);
                }
                else{
                    int ta = in.nextInt();
                    if(type == 11){
                        globalZones.get(ind).addEvent(new GiveEvent(message, isSilent == 1, Dilet, globalItems.get(ta), -1,-1));
                    }
                    if(type == 12){
                        globalZones.get(ind).addEvent(new TakeEvent(message, isSilent == 1, Dilet, globalItems.get(ta), -1,-1));
                    }
                    System.out.println(message+" "+isSilent+" "+questID+" "+questStep+" "+ta);
                }
            }
        }
        catch(FileNotFoundException e){
            return;
        }
    }
    public static void loadInteractables(String directory, int ind){
        try{
            File f = new File(directory+"INTERACTABLES.txt");
            Scanner in = new Scanner(f);
            int num = in.nextInt();
            for(int i = 0; i < num; i++){
                int len = in.nextInt();
                int height = in.nextInt();
                int event = in.nextInt();
                globalZones.get(ind).loadInteractable(new InteractableObject(len * resScalar, height * resScalar, event));
                System.out.println(len +" "+height+" "+event);
            }
        }
        catch(FileNotFoundException e){
            return;
        }
    }
    public static void loadNPCs(String directory, int ind){
        try{
            File f = new File(directory+"NPCS.txt");
            Scanner in = new Scanner(f);
            int n = in.nextInt();
            System.out.println(n);
            String chaff = in.nextLine();
            for(int i = 0; i < n; i++){
                String name = in.nextLine();
                int len = in.nextInt();
                int height = in.nextInt();
                int id = in.nextInt();
                int options = in.nextInt();
                ArrayList<DialogueOption> deez = new ArrayList<>();
                for(int j = 0; j < options; j++){
                    int adjacent = in.nextInt();
                    ArrayList<Integer> adj = new ArrayList<>();
                    for(int k = 0; k < adjacent; k++) {
                        adj.add(in.nextInt());
                    }
                    int dialogueID = in.nextInt();
                    chaff = in.nextLine();
                    String label = in.nextLine();
                    deez.add(new DialogueOption(adj, globalZones.get(ind).getEvent(dialogueID), label));
                    System.out.println(dialogueID+" "+label);
                }
                globalZones.get(ind).loadNPC(new NPC(len * resScalar, height* resScalar, id, deez.get(0), name, i));
                System.out.println(len+" "+height+" "+id+" "+name+" "+i);
                globalZones.get(ind).addDialogueOption(deez);
            }
        }
        catch(FileNotFoundException e){
            return;
        }
    }
    public static void loadCutscenes(String directory, int ind){
        try{
            File f = new File(directory+"CUTSCENES.txt");
            Scanner in = new Scanner(f);
            int n = in.nextInt();
            for(int i = 0; i < n; i++){
                int scenes = in.nextInt();
                Cutscene c = new Cutscene(Dilet);
                for(int j = 0; j < scenes; j++) {
                    c.addEvent(globalZones.get(ind).getEvent(in.nextInt()), in.nextInt());
                }
                globalZones.get(ind).addCutscene(c);
            }
        }
        catch(FileNotFoundException e){
            return;
        }
    }
    public static void loadChests(String directory, int ind){
        try{
            File f = new File(directory+"CHESTS.txt");
            Scanner in = new Scanner(f);
            int n = in.nextInt();
            for(int i = 0; i < n; i++){
                int x = in.nextInt();
                int y = in.nextInt();
                int items = in.nextInt();
                ArrayList<Item> list = new ArrayList<>();
                for(int j = 0; j < items; j++){
                    list.add(globalItems.get(in.nextInt()));
                }
                globalZones.get(ind).loadChest(new Chest(x* resScalar,y* resScalar,list,Dilet));
                System.out.println("loaded a chest");
            }
        }
        catch(FileNotFoundException e){
            return;
        }
    }

    public static void main(String[] args) {
        cutSceneInd = 0;
        newStart = 0;
        curState = 9;
        globalItems.add(new Item("Feesh", "Glorious"));
        load();
        PApplet.main("Main");
    }
    public void settings(){
        size(640* resScalar,360* resScalar);
    }
    public void setup(){

        frameRate(60);
        questB = loadImage("baseData/GLOBAL/UI/questlogbutton.png");
        questB.resize(60 * resScalar, 0);
        questT = loadImage("baseData/GLOBAL/UI/questTabBackground.png");
        questT.resize(200 * resScalar, 0);
        itemB = loadImage("baseData/GLOBAL/UI/itembutton.png");
        itemB.resize(60 * resScalar, 0);
        pauseB = loadImage("baseData/GLOBAL/UI/pausebutton.png");
        pauseB.resize(60 * resScalar, 0);
        butB = loadImage("baseData/GLOBAL/UI/bottomBarBackground.png");
        butB.resize(200 * resScalar, 0);
        evB = loadImage("baseData/GLOBAL/UI/eventLogBackground.png");
        evB.resize(200 * resScalar, 0);
        cons = createFont("baseData/CONSOLA.TTF", 10);
        textFont(cons);
    }
    public void draw(){
        elapsedTime = millis();
        inputProcess();
        if(curState != 1 && curState != 9){
            up = false;
            down = false;
            left = false;
            right = false;
        }
        if(curState == 1 || curState == 3 || curState == 4 || curState == 7 || curState == 9){
            background(0,255,0);
            noStroke();
            fill(255,255,255);
            drawPlayer();
            drawZone();
        }
        if(curState == 2){
            menu();
        }
        drawBanner();
        if(curState == 7){
            textSize(10 * resScalar);
            if(millis()-newStart < curZone.getCutscene(curCutscene).getDelays().get(cutSceneInd) || !curZone.getCutscene(curCutscene).getSeq().get(cutSceneInd).done()){
                text(curZone.getEvent(curCutscene).getSeq().get(cutSceneInd).message(),15 * resScalar,15 * resScalar);
            }
            else{
                cutSceneInd++;
                newStart = millis();
                if(cutSceneInd == curZone.getCutscene(curCutscene).getDelays().size()){
                    curState = 1;
                }
            }
        }
        if(curState == 3){
            textSize(10 * resScalar);
            text(curZone.getEvent(curInteractive.msg()).message(),15 * resScalar,15 * resScalar);
        }
        if(curState == 4){
            drawDialogue();
        }
        if(curState == 5){
            chestMenu();
        }
        if(curState == 8){
            inventoryMenu();
        }
        if(curState == 9){
            combatDrawer();
            combatHandler();
            /**
             *if(millis() - sinceLastProjectile > 1000){
                activeProjectiles.add(new Projectile(10, 5, 2, (3 * Math.PI)/4));
                projCoords.add(new Coordinate(400 ,320));
                activeProjectiles.add(new Projectile(10, 5, 2, (Math.PI)/4));
                projCoords.add(new Coordinate(400 ,320));
                activeProjectiles.add(new Projectile(10, 5, 2, (5 * Math.PI)/4));
                projCoords.add(new Coordinate(400 ,320));
                activeProjectiles.add(new Projectile(10, 5, 2, (7 * Math.PI)/4));
                projCoords.add(new Coordinate(400 ,320));
                sinceLastProjectile = millis();
            }
             **/
            if(enemies.isEmpty()){
                System.out.println("ADDING BANDIT");
                enemies.add(new Enemy(20,30,2,5));
                ecoords.add(new Coordinate(300,500));
                curAttk.add(1);
                Attack a1 = new Attack();
                Attack a2 = new Attack();
                a1.addStep(5000, 500, new ArrayList<Projectile>(){{add(new Projectile(10, 10, 5, Math.PI/4));add(new Projectile(10, 10, 5, 3 * Math.PI/4));add(new Projectile(10, 10, 5, 5 * Math.PI/4));add(new Projectile(10, 10, 5, 7 * Math.PI/4));}});
                a1.addStep(5000, 250, new ArrayList<Projectile>(){{add(new Projectile(10, 10, 3, Math.PI/2));
                    add(new Projectile(10, 10, 3, Math.PI));
                    add(new Projectile(10, 10, 3, 3 * Math.PI/2));
                    add(new Projectile(10, 10, 3, 2 * Math.PI));}});
                a2.addStep(2000, 1000, new ArrayList<Projectile>(){{
                    add(new Projectile(10, 10, 10, -(2 * Math.PI) / 3));
                    add(new Projectile(10, 10, 10,  -(Math.PI) / 3));
                    add(new Projectile(10, 10, 10,  -(Math.PI) / 2));
                    add(new Projectile(10, 10, 10, -(7 * Math.PI) / 12));
                    add(new Projectile(10, 10, 10, -(5 * Math.PI) / 12));
                    add(new Projectile(10, 10, 10, -(13 * Math.PI) / 24));
                    add(new Projectile(10, 10, 10, -(11 * Math.PI) / 24));
                }});
                enemies.get(0).addAtk(a1);
                enemies.get(0).addAtk(a2);
            }

        }

    }
    public void drawDialogue(){
        fill(0,0,0);
        textFont(cons);
        textSize(10 * resScalar);
        Event i = curDialogueOption.returnTrigger();
        if(i.returnQuestID() != -1) {
            globalQuests.get(i.returnQuestID()).progress(i.returnQuestStep());
            if(globalQuests.get(i.returnQuestID()).questDone()){
                questLog.remove(i);
            }
        }
        text(curDialogueOption.use(), 450 * resScalar,130 * resScalar, 630 * resScalar, 160 * resScalar);
        int counter = 0;
        textSize(20 * resScalar);
        for(int co : curDialogueOption.getAdj()){
            fill(255,0,127);
            rectMode(CORNERS);
            rect(450 * resScalar,(165 + (35 * counter))* resScalar,630 * resScalar,(195 + (35  * counter))* resScalar);
            fill(0,0,0);
            textFont(cons);
            textSize(10 * resScalar);
            text(curZone.getDialogue(co, curNPC.getIndex()).returnLabel(),450 * resScalar,(165 + (35 * counter))* resScalar,630 * resScalar,(195 + (35  * counter))* resScalar);
            counter++;
        }
    }
    public void drawBanner(){
        textSize(10 * resScalar);
        rectMode(CORNERS);
        imageMode(CORNER);
        image(evB, 440 * resScalar, 120 * resScalar);
        image(questT, 440 * resScalar, 0);
        image(butB, 440 * resScalar, 330 * resScalar);
        image(itemB, 445 * resScalar, 335 * resScalar);
        image(pauseB, 510 * resScalar, 335 * resScalar);
        image(questB, 575 * resScalar, 335 * resScalar);
        if(!questLog.isEmpty()){
            text(questLog.get(0).getName()+": "+questLog.get(0).curStep(), 460 * resScalar, 20 * resScalar, 620 * resScalar, 100 * resScalar);
        }
        else{
            text("No active quest", 460 * resScalar, 20 * resScalar, 620 * resScalar, 100 * resScalar);
        }
        if(curState != 4){
            fill(0,0,0);
            int ctr = 0;
            for(int i = pastEvs.size() -1 ; i >= 0;i--){
                String s = pastEvs.get(i);
                text(s, 450 * resScalar, (280 - 30 * ctr)* resScalar,630* resScalar,(310 - 30 * ctr)* resScalar);
                ctr++;
            }
        }
    }
    public void menu(){
        fill(0,255,255);
        rect(0,0,440 * resScalar,360 * resScalar);
        fill(0,0,0);
        rect(260 * resScalar, 90* resScalar, 380* resScalar, 120* resScalar);
        fill(255,255,255);
        textAlign(CENTER);
        textSize(30 * resScalar);
        text("MAIN MENU", 260 * resScalar, 90 * resScalar, 380 * resScalar, 120 * resScalar);
        fill(0,0,0);
        rect(260 * resScalar, 180* resScalar, 380* resScalar, 210* resScalar);
        fill(255,255,255);
        text("SAVE MENU", 260 * resScalar, 180* resScalar, 380* resScalar, 210* resScalar);


    }
    public void chestMenu(){
        fill(127,127,127);
        rect(120 * resScalar, 100 * resScalar, 525 * resScalar, 305 * resScalar);
        fill(64,64,64);
        int counter = 0;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 2; j++){
                if(counter < curChest.getContents().size()){
                    fill(32,32,32);
                }
                else{
                    fill(64,64,64);
                }
                rect((125 + (100 * i)) * resScalar, (105 + (100 * j)) * resScalar, (220 + (100 * i)) * resScalar, (200 + (100 * j)) * resScalar);
                counter++;
            }
        }

    }
    public void inventoryMenu(){
        fill(127,127,127);
        rect(120 * resScalar, 100 * resScalar, 525 * resScalar, 305 * resScalar);
        fill(64,64,64);
        int counter = 0;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 2; j++){
                if(counter < Dilet.getInventory().size()){
                    fill(32,32,32);
                }
                else{
                    fill(64,64,64);
                }
                rect((125 + (100 * i)) * resScalar, (105 + (100 * j)) * resScalar, (220 + (100 * i)) * resScalar, (200 + (100 * j)) * resScalar);
                counter++;
            }
        }
    }
    public void drawZone(){
        int ctr =0;
        for(WorldObject obj : curZone.getZoneWorldObjects()){
            fill(255,255,255);
            rectMode(CORNERS);
            rect((float)curZone.getZoneWorldObjectCoords(ctr).getX(), (float)curZone.getZoneWorldObjectCoords(ctr).getY() ,(float)curZone.getZoneWorldObjectCoords(ctr).getX()+ obj.getLen(),(float)curZone.getZoneWorldObjectCoords(ctr).getY()+ obj.getHeight());
            ctr++;
        }
        ctr = 0;
        for(InteractableObject obj : curZone.getZoneInteractableObjects()){
            rectMode(CORNERS);
            fill(0,255,255);
            rect((float)curZone.getZoneInteractableObjectCoords(ctr).getX(), (float)curZone.getZoneInteractableObjectCoords(ctr).getY() ,(float)curZone.getZoneInteractableObjectCoords(ctr).getX()+ obj.getLen(),(float)curZone.getZoneInteractableObjectCoords(ctr).getY()+ obj.getHeight());
            ctr++;
        }
        ctr =0;
        for(NPC obj : curZone.getNPCs()){
            rectMode(CORNERS);
            fill(255,0,255);
            rect((float)curZone.getZoneNPCCoords(ctr).getX(), (float)curZone.getZoneNPCCoords(ctr).getY() ,(float)curZone.getZoneNPCCoords(ctr).getX()+ obj.getLen(),(float)curZone.getZoneNPCCoords(ctr).getY()+ obj.getHeight());
            ctr++;
        }
        ctr=0;
        for(Chest obj : curZone.getZoneChests()){
            rectMode(CORNERS);
            fill(255,255,0);
            rect((float)curZone.getZoneChestCoords(ctr).getX(), (float)curZone.getZoneChestCoords(ctr).getY() ,(float)curZone.getZoneChestCoords(ctr).getX()+ obj.getLen(),(float)curZone.getZoneChestCoords(ctr).getY()+ obj.getHeight());
            ctr++;
        }
    }
    public void drawPlayer() {
        fill(0, 230, 172);
        rectMode(CENTER);
        rect((float)Dilet.getX(), (float)Dilet.getY(), 30* resScalar, 30* resScalar);
    }
    public void inputProcess(){
        if(curState == 1 || curState == 9){
            if (up) {
                boolean uflag = true;
                int ctr = 0;
                for(WorldObject w : curZone.getZoneWorldObjects()){
                    if(!Dilet.canUp(w,curZone.getZoneWorldObjectCoords(ctr)) ){
                        uflag = false;
                    }
                    ctr++;
                }
                ctr = 0;
                for(Chest w : curZone.getZoneChests()){
                    if(!Dilet.canUp(w, curZone.getZoneChestCoords(ctr))){
                        uflag = false;
                    }
                    ctr++;
                }
                ctr= 0;
                for(NPC w : curZone.getNPCs()){
                    if(!Dilet.canUp(w, curZone.getZoneNPCCoords(ctr))){
                        uflag = false;
                    }
                    ctr++;
                }
                ctr = 0;
                for(InteractableObject w : curZone.getZoneInteractableObjects()){
                    if(!Dilet.canUp(w, curZone.getZoneInteractableObjectCoords(ctr))){
                        uflag = false;
                    }
                    ctr++;
                }
                if(uflag){
                    Dilet.moveY(-5 * resScalar);
                }
                if(Dilet.getY() < 15 * resScalar){
                    if(curZone.getN() == -1){
                        Dilet.moveY(5 * resScalar);
                    }
                    else{
                        curZone = globalZones.get(curZone.getN());
                        Dilet.setLoc(curZone.enterSouth().getX() * resScalar, curZone.enterSouth().getY() * resScalar);
                    }
                }
            }
            if (left) {
                boolean lflag = true;
                int ctr = 0;
                for(WorldObject w : curZone.getZoneWorldObjects()){
                    if(!Dilet.canLeft(w,curZone.getZoneWorldObjectCoords(ctr)) ){
                        lflag = false;
                    }
                    ctr++;
                }
                ctr = 0;
                for(Chest w : curZone.getZoneChests()){
                    if(!Dilet.canLeft(w, curZone.getZoneChestCoords(ctr))){
                        lflag = false;
                    }
                    ctr++;
                }
                ctr= 0;
                for(NPC w : curZone.getNPCs()){
                    if(!Dilet.canLeft(w, curZone.getZoneNPCCoords(ctr))){
                        lflag = false;
                    }
                    ctr++;
                }
                ctr = 0;
                for(InteractableObject w : curZone.getZoneInteractableObjects()){
                    if(!Dilet.canLeft(w, curZone.getZoneInteractableObjectCoords(ctr))){
                        lflag = false;
                    }
                    ctr++;
                }
                if(lflag){
                    Dilet.moveX(-5 * resScalar);
                }
                if(Dilet.getX() < 15 * resScalar){
                    if(curZone.getW() == -1){
                        Dilet.moveX(5 * resScalar);
                    }
                    else{
                        int newInd = curZone.getW();
                        curZone = globalZones.get(newInd);
                        Dilet.setLoc(globalZones.get(newInd).enterEast().getX() * resScalar, globalZones.get(newInd).enterEast().getY() * resScalar);
                    }
                }
            }
            if (right) {
                boolean rflag = true;
                int ctr = 0;
                for(WorldObject w : curZone.getZoneWorldObjects()){
                    if(!Dilet.canRight(w,curZone.getZoneWorldObjectCoords(ctr)) ){
                        rflag = false;
                    }
                    ctr++;
                }
                ctr = 0;
                for(Chest w : curZone.getZoneChests()){
                    if(!Dilet.canRight(w, curZone.getZoneChestCoords(ctr))){
                        rflag = false;
                    }
                    ctr++;
                }
                ctr= 0;
                for(NPC w : curZone.getNPCs()){
                    if(!Dilet.canRight(w, curZone.getZoneNPCCoords(ctr))){
                        rflag = false;
                    }
                    ctr++;
                }
                ctr = 0;
                for(InteractableObject w : curZone.getZoneInteractableObjects()){
                    if(!Dilet.canRight(w, curZone.getZoneInteractableObjectCoords(ctr))){
                        rflag = false;
                    }
                    ctr++;
                }
                if(rflag){
                    Dilet.moveX(5 * resScalar);
                }
                if(Dilet.getX() > 425 * resScalar){
                    if(curZone.getE() == -1){
                        Dilet.moveX(-5 * resScalar);
                    }
                    else{
                        int newInd = curZone.getE();
                        curZone = globalZones.get(newInd);
                        Dilet.setLoc(globalZones.get(newInd).enterWest().getX() * resScalar, globalZones.get(newInd).enterWest().getY() * resScalar);
                        System.out.println("BALLS");
                    }
                }
            }
            if (down) {
                boolean dflag = true;
                int ctr = 0;
                for(WorldObject w : curZone.getZoneWorldObjects()){
                    if(!Dilet.canDown(w,curZone.getZoneWorldObjectCoords(ctr)) ){
                        dflag = false;
                    }
                    ctr++;
                }
                ctr = 0;
                for(Chest w : curZone.getZoneChests()){
                    if(!Dilet.canDown(w, curZone.getZoneChestCoords(ctr))){
                        dflag = false;
                    }
                    ctr++;
                }
                ctr= 0;
                for(NPC w : curZone.getNPCs()){
                    if(!Dilet.canDown(w, curZone.getZoneNPCCoords(ctr))){
                        dflag = false;
                    }
                    ctr++;
                }
                ctr = 0;
                for(InteractableObject w : curZone.getZoneInteractableObjects()){
                    if(!Dilet.canDown(w, curZone.getZoneInteractableObjectCoords(ctr))){
                        dflag = false;
                    }
                    ctr++;
                }
                if(dflag){
                    Dilet.moveY(5 * resScalar);
                }
                if(Dilet.getY() > 345 * resScalar){
                    if(curZone.getS() == -1){
                        Dilet.moveY(-5 * resScalar);
                    }
                    else{
                        curZone = globalZones.get(curZone.getS());
                        Dilet.setLoc(curZone.enterNorth().getX() * resScalar, curZone.enterNorth().getY() * resScalar);
                    }
                }
            }

        }
    }
    public void mouseClicked() {
        System.out.println(mouseX+" "+mouseY);
        System.out.println(curZone.getZoneWorldObjects().size());
        for(WorldObject w : curZone.getZoneWorldObjects()){
            System.out.println(w.toString());
        }
        System.out.println(curZone.getZoneInteractableObjects().size());
        System.out.println("CURRENT PROJECTILES: "+ projCoords.size());
        for(InteractableObject i : curZone.getZoneInteractableObjects()){
            System.out.println(i.toString());
        }
        if(curState == 1){
            if(mouseX >= 510* resScalar && mouseX <= 570* resScalar && mouseY >= 335* resScalar && mouseY <= 355* resScalar){
                curState = 2;
                System.out.println("PAUSED");
                for(Item i : Dilet.getInventory()){
                    System.out.println(i.toString());
                }
            }
            int ctr = 0;
            for(InteractableObject i : curZone.getZoneInteractableObjects()){
                if(mouseX >= curZone.getZoneInteractableObjectCoords(ctr).getX() && mouseX <= curZone.getZoneInteractableObjectCoords(ctr).getX() + i.getLen() && mouseY >= curZone.getZoneInteractableObjectCoords(ctr).getY() && mouseY <= curZone.getZoneInteractableObjectCoords(ctr).getY() + i.getHeight()){
                    if(Math.abs(Dilet.getX() - (curZone.getZoneInteractableObjectCoords(ctr).getX()+ (i.getLen()/2))) < 60 * resScalar){
                        if(Math.abs(Dilet.getY() - (curZone.getZoneInteractableObjectCoords(ctr).getY()+ (i.getHeight()))) < 60 * resScalar){
                            if(curZone.getEvent(i.msg()).isCutscene()){
                                curState = 7;
                                newStart = millis();
                                System.out.println("START: "+millis());
                                cutSceneInd = 0;
                                curCutscene = i.msg();
                                for(Integer time : curZone.getCutscene(curCutscene).getDelays()){
                                    System.out.println(time);
                                }
                            }
                            else{
                                if(curZone.getEvent(i.msg()).returnQuestID() != -1){
                                    globalQuests.get(curZone.getEvent(i.msg()).returnQuestID()).progress(curZone.getEvent(i.msg()).returnQuestStep());
                                    if(globalQuests.get(curZone.getEvent(i.msg()).returnQuestID()).questDone()){
                                        questLog.remove(globalQuests.get(curZone.getEvent(i.msg()).returnQuestID()));
                                    }
                                }
                                curState = 3;
                                curInteractive = i;
                                if(pastEvs.size() == 6){
                                    pastEvs.set(0, pastEvs.get(1));
                                    pastEvs.set(1, pastEvs.get(2));
                                    pastEvs.set(2, pastEvs.get(3));
                                    pastEvs.set(3, pastEvs.get(4));
                                    pastEvs.set(4, pastEvs.get(5));
                                    pastEvs.set(5, curZone.getEvent(i.msg()).message());
                                }
                                else{
                                    pastEvs.add(curZone.getEvent(i.msg()).message());
                                }
                            }
                        }
                    }
                }
                ctr++;
            }
            ctr = 0;
            for(NPC n : curZone.getNPCs()){
                if(mouseX >= curZone.getZoneNPCCoords(ctr).getX() && mouseX <= curZone.getZoneNPCCoords(ctr).getX() + n.getLen() && mouseY >= curZone.getZoneNPCCoords(ctr).getY() && mouseY <= curZone.getZoneNPCCoords(ctr).getY() + n.getHeight()){
                    if(Math.abs(Dilet.getX() - (curZone.getZoneNPCCoords(ctr).getX()+ (n.getLen()/2))) < 60 * resScalar){
                        if(Math.abs(Dilet.getY() - (curZone.getZoneNPCCoords(ctr).getY()+ (n.getHeight()))) < 60 * resScalar){
                            System.out.println("NPC ACTIVE");
                            curState = 4;
                            curNPC = n;
                            curDialogueOption = n.talk();
                            if(pastEvs.size() == 6){
                                pastEvs.set(0, pastEvs.get(1));
                                pastEvs.set(1, pastEvs.get(2));
                                pastEvs.set(2, pastEvs.get(3));
                                pastEvs.set(3, pastEvs.get(4));
                                pastEvs.set(4, pastEvs.get(5));
                                pastEvs.set(5, "Talked to "+curNPC.getName());
                            }
                            else{
                                pastEvs.add("Talked to "+curNPC.getName());
                            }
                        }
                    }
                }
                ctr++;
            }
            ctr =0;
            for(Chest c : curZone.getZoneChests()){
                if(mouseX >= curZone.getZoneChestCoords(ctr).getX() && mouseX <= curZone.getZoneChestCoords(ctr).getX() + c.getLen() && mouseY >= curZone.getZoneChestCoords(ctr).getY() && mouseY <= curZone.getZoneChestCoords(ctr).getY() + c.getHeight()){
                    if(Math.abs(Dilet.getX() - (curZone.getZoneChestCoords(ctr).getX() + (c.getLen()/2))) < (15 + c.getLen()/2 + 30) * resScalar){
                        if(Math.abs(Dilet.getY() - (curZone.getZoneChestCoords(ctr).getY()+ (c.getHeight()/2))) < (15 + c.getHeight()/2 + 30) * resScalar){
                            System.out.println("CHEST ACTIVE");
                            curState = 5;
                            curChest = c;
                            if(pastEvs.size() == 6){
                                pastEvs.set(0, pastEvs.get(1));
                                pastEvs.set(1, pastEvs.get(2));
                                pastEvs.set(2, pastEvs.get(3));
                                pastEvs.set(3, pastEvs.get(4));
                                pastEvs.set(4, pastEvs.get(5));
                                pastEvs.set(5, "Opened chest");
                            }
                            else{
                                pastEvs.add("Opened chest");
                            }
                        }
                    }
                }
                ctr++;
            }
        }
        else if(curState == 2){
            if(mouseX >= 510* resScalar && mouseX <= 570* resScalar && mouseY >= 335* resScalar && mouseY <= 355* resScalar){
                curState = 1;
                System.out.println("UNPAUSED");
            }
        }
        else if(curState == 3){
            curZone.getEvent(curInteractive.msg()).reset();
            System.out.println("EXITING INTERACT");//to be replaced with dialogue n stuff
            curState = 1;
        }
        else if(curState == 4){
            if(mouseX >= 450 * resScalar && mouseX <= 630 * resScalar){
                for(int i = 0; i < curDialogueOption.getAdj().size(); i++){
                    if(mouseY >= (130 + (30 * i))* resScalar && mouseY <= (160 + (30 * i))* resScalar){
                        curDialogueOption = curZone.getDialogue(curDialogueOption.getAdj().get(i), curNPC.getIndex());
                        System.out.println("You selected dialogue option "+i);
                        return;
                    }
                }
            }
            curState = 1;
            System.out.println("Exited dialogue");

        }
        else if(curState == 5){
            int counter =  0;
            if(mouseX >= 120 * resScalar && mouseY >= 100 * resScalar && mouseX <= 600* resScalar && mouseY <= 300* resScalar){
                for(int i = 0; i < 4; i++){
                    for(int j = 0; j < 2; j++){
                        if(counter < curChest.getContents().size()){
                            if(mouseX >=(125 + (100 * i)) * resScalar && mouseY >= (105 + (100 * j)) * resScalar && mouseX <= (220 + (100 * i)) * resScalar && mouseY <=  (200 + (100 * j)) * resScalar){
                                System.out.println(i * 2 + j + 1);
                                if(pastEvs.size() == 6){
                                    pastEvs.set(0, pastEvs.get(1));
                                    pastEvs.set(1, pastEvs.get(2));
                                    pastEvs.set(2, pastEvs.get(3));
                                    pastEvs.set(3, pastEvs.get(4));
                                    pastEvs.set(4, pastEvs.get(5));
                                    pastEvs.set(5, "Obtained "+curChest.getContents().get(counter).getName());
                                }
                                else{
                                    pastEvs.add("Obtained "+curChest.getContents().get(counter).getName());
                                }
                                Dilet.addItem(curChest.getContents().get(counter));
                                curChest.removeItem(curChest.getContents().get(counter));
                            }
                            counter++;
                        }
                    }
                }
            }
            else{
                System.out.println("EXITING CHEST");
                curState = 1;
            }
        }
        else if(curState == 8){
            int counter =  0;
            if(mouseX >= 120 * resScalar && mouseY >= 100 * resScalar && mouseX <= 600* resScalar && mouseY <= 300* resScalar){
                for(int i = 0; i < 4; i++){
                    for(int j = 0; j < 2; j++){
                        if(counter < Dilet.getInventory().size()){
                            if(mouseX >=(125 + (100 * i)) * resScalar && mouseY >= (105 + (100 * j)) * resScalar && mouseX <= (220 + (100 * i)) * resScalar && mouseY <=  (200 + (100 * j)) * resScalar){
                                System.out.println(i * 2 + j + 1);
                                System.out.println("SELECTED ITEM: "+ Dilet.getInventory().get(counter));
                            }
                            counter++;
                        }
                    }
                }
            }
        }
        else if(curState == 9){
            if(millis() - sinceLastProjectile > 100){
                double angle = Math.atan((mouseY - Dilet.getY())/(mouseX - Dilet.getX()));
                if(mouseX - Dilet.getX() < 0){
                    angle+=Math.PI;
                }
                activeProjectiles.add(new Projectile(10, 5, 12, angle));
                projCoords.add(new Coordinate(Dilet.getX() + (2 * Dilet.getRadius() * Math.cos(angle)),Dilet.getY() + (2 * Dilet.getRadius() * Math.sin(angle))));
                sinceLastProjectile = millis();
            }
        }
    }
    public void keyPressed(){
        if(curState == 1 || curState == 9){
            if(key == 'w'){
                up = true;
            }
            if(key == 's'){
                down = true;
            }
            if(key == 'd'){
                right = true;
            }
            if(key == 'a'){
                left = true;
            }
            if(key == 'e'){
                curState = 8;
            }
        }
        else if(curState == 8){
            if(key == 'e'){
                curState = 1;
            }
        }
    }
    public void keyReleased(){
        if(curState == 1 || curState == 9){
            if(key == 'w'){
                up = false;
            }
            if(key == 's'){
                down = false;
            }
            if(key == 'd'){
                right = false;
            }
            if(key == 'a'){
                left = false;
            }
        }
    }
    public void combatHandler(){
        for(int i = 0; i < enemies.size(); i++){
            ArrayList<Projectile> atk = enemies.get(i).getAtk(curAttk.get(i), millis());
            if(atk != null){
                activeProjectiles.addAll(atk);
                for(Projectile p : atk){
                    projCoords.add(new Coordinate(ecoords.get(i).getX() + 2 * (enemies.get(i).getRadius() * Math.cos(p.getAngle())), ecoords.get(i).getY() + 2 * (enemies.get(i).getRadius() * Math.sin(p.getAngle()))));
                }
            }
            else if(enemies.get(i).attackOver){
                int newAttack = (int)(Math.random() * enemies.get(i).getPatterns());
                if(enemies.get(i).getPatterns() > 1){
                    while(newAttack == curAttk.get(i)){
                        newAttack = (int)(Math.random() * enemies.get(i).getPatterns());
                    }
                }
                curAttk.set(i, newAttack);
                System.out.println("New Attack: "+i);
            }
        }
        for(int i = 0; i < activeProjectiles.size(); i++){
            Coordinate c = projCoords.get(i);
            c.addX(activeProjectiles.get(i).getDX());
            c.addY(activeProjectiles.get(i).getDY());
            if(Math.abs(c.getX() - Dilet.getX()) < activeProjectiles.get(i).radius + Dilet.getRadius() && Math.abs(c.getY() - Dilet.getY()) < activeProjectiles.get(i).radius + Dilet.getRadius()){
                health-=activeProjectiles.get(i).damage;
                System.out.println("Took damage!");
                projCoords.remove(i);
                activeProjectiles.remove(i);
                i--;
                continue;
            }
            else if(c.getX() > 440 * resScalar || c.getX() < 0 || c.getY() > 360 * resScalar || c.getY() < 0){ //for reasons of computational efficiency, projectiles go through walls
                projCoords.remove(i);
                activeProjectiles.remove(i);
                i--;
                continue;
            }
            for(int j = 0; j < enemies.size(); j++){
                Enemy e = enemies.get(j);
                Coordinate ec = ecoords.get(j);
                if(Math.abs(c.getX() - ec.getX()) < activeProjectiles.get(i).radius + e.getRadius() && Math.abs(c.getY() - ec.getY()) < activeProjectiles.get(i).radius + e.getRadius()){
                    e.takeDamage(activeProjectiles.get(i).getDamage());
                    System.out.println("Enemy "+j+" Took damage!");
                    projCoords.remove(i);
                    activeProjectiles.remove(i);
                    i--;
                }
            }
        }
        for(int i = 0; i < enemies.size(); i++){
            if(enemies.get(i).getHealth() <= 0){
                enemies.remove(i);
                ecoords.remove(i);
                curAttk.remove(i);
                i--;
            }
        }
//        for(int i = 0; i < enemies.size(); i++){ //enemy movement handling, to be implemented further later
//            double angle = ((2 *Math.random())) * Math.PI;
//            System.out.println(angle);
//            ecoords.get(i).addX(Math.cos(angle) * enemies.get(i).getSpeed());
//            ecoords.get(i).addY(Math.sin(angle) * enemies.get(i).getSpeed());
//        }

    }
    public void combatDrawer(){
        int ctr = 0;
        for(Coordinate c : projCoords) {
            rectMode(CENTER);
            fill(255, 255, 255);
            rect((float) c.getX(), (float) c.getY(), activeProjectiles.get(ctr).radius * 2, activeProjectiles.get(ctr).radius * 2);
            ctr++;
        }
        ctr = 0;
        for(Coordinate c : ecoords){
            fill(255,0,0);
            rectMode(CENTER);
            rect((float)c.getX(), (float)c.getY(), (float)enemies.get(ctr).getRadius() * 2 * resScalar, (float)enemies.get(ctr).getRadius()* 2 * resScalar);
            ctr++;
        }
    }
}