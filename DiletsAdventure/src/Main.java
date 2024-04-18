import processing.core.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.*;
public class Main extends PApplet {
    //all of our fun vars
    static ArrayList<Zone> globalZones = new ArrayList<>();
    static ArrayList<Event> globalEvents = new ArrayList<>();
    static ArrayList<Item> globalItems = new ArrayList<>();
    static ArrayList<InteractableObject> globalInteractives = new ArrayList<>();
    static ArrayList<WorldObject> globalObjects = new ArrayList<>();
    static ArrayList<DialogueOption> globalDialogueOptions = new ArrayList<>();
    static ArrayList<Chest> globalChests = new ArrayList<>();
    static ArrayList<NPC> globalNPCs = new ArrayList<>();
    static ArrayList<Quest> globalQuests = new ArrayList<>();
    static ArrayList<Quest> questLog = new ArrayList<>();
    public static int resScalar = 2;
    static Player Dilet = new Player(resScalar);
    static int curState = 1; //state -1 is intro cutscene. state 0 is menu. state 1 is in-game. state 2 is pause menu. state 3 is interacting with an object. state 4 is dialogue. state 5 is chest. state 6 is inventory. state 7 is cutscene, state 8 is inventory, more to come
    static Zone curZone = null;
    static InteractableObject curInteractive = null;
    static NPC curNPC = null;
    static DialogueOption curDialogueOption = null;
    static Chest curChest = null;
    static int curCutscene;
    static int elapsedTime;
    static int newStart; //updated each time the cutscene state is entered
    static int cutSceneInd; //ditto above
    static boolean up, left, right, down;
    //TEST AREA
    //END TEST
    public static void load(){
        loadItems();
        loadEvents();
        loadQuests();
        loadDialogue();
        loadCutscenes();
        loadNPC();
        loadChest();
        loadInteractable();
        loadWorldObj();
        loadZone();
        curZone = globalZones.get(0);
    }
    public static void loadEvents(){
        try{
            File ev1 = new File("baseData/EVENT1.txt");
            Scanner ev1in = new Scanner(ev1);
            int ev1n = ev1in.nextInt();
            for(int i = 0; i < ev1n; i++){
                System.out.println("PASS");
                int type = ev1in.nextInt();
                int questID = ev1in.nextInt();
                int questStep = ev1in.nextInt();
                String chaff = ev1in.nextLine();
                String message = ev1in.nextLine();
                String isSilent = ev1in.nextLine();
                System.out.println("TYPE: " +type);
                if(type == 1){
                    globalEvents.add(new Event(message, (Objects.equals(isSilent, "SILENT")), Dilet, questID, questStep));
                }
                if(type == 2 || type == 3){
                    Item ta = globalItems.get(ev1in.nextInt());
                    if(type == 2){
                        globalEvents.add(new TakeEvent(message, isSilent.equals("SILENT"), Dilet, ta, questID, questStep));
                    }
                    else{
                        globalEvents.add(new GiveEvent(message, isSilent.equals("SILENT"), Dilet, ta, questID, questStep));
                    }
                }
                if(type == 4 || type == 5 || type == 6 || type == 7 || type == 8 || type == 9 || type == 10 || type == 11 || type == 12){
                    int zone = ev1in.nextInt();
                    int ind = ev1in.nextInt();
                    int x = ev1in.nextInt();
                    int y = ev1in.nextInt();
                    if(type == 4){
                        globalEvents.add(new RemoveEvent(Dilet, zone, ind, x,y, questID, questStep, globalZones, globalObjects));
                    }
                    if(type == 5){
                        globalEvents.add(new PlaceEvent(Dilet, zone, ind, x,y, questID, questStep,globalZones, globalObjects));
                    }
                    if(type == 6){
                        globalEvents.add(new removeInteractable(Dilet, zone, ind, x,y, questID, questStep,globalZones, globalInteractives));
                    }
                    if(type == 7){
                        globalEvents.add(new placeInteractable(Dilet, zone, ind, x,y, questID, questStep,globalZones, globalInteractives));
                    }
                    if(type == 8){
                        globalEvents.add(new removeNPC(Dilet, zone, ind, questID, questStep,globalZones, globalNPCs));
                    }
                    if(type == 9){
                        globalEvents.add(new placeNPC(Dilet, zone, ind, x,y, questID, questStep,globalZones, globalNPCs));
                    }
                    if(type > 9){
                        int speed = ev1in.nextInt();
                        if(type == 10){
                            globalEvents.add(new moveObject(Dilet, zone, ind, x, y, speed, questID, questStep,globalZones, globalObjects));
                        }
                        if(type == 11){
                            globalEvents.add(new moveInteractable(Dilet, zone, ind, x, y, speed, questID, questStep,globalZones, globalInteractives));
                        }
                        if(type == 12){
                            globalEvents.add(new moveNPC(Dilet, zone, ind, x, y, speed, questID, questStep,globalZones, globalNPCs));
                        }
                    }
                }
                System.out.println("ADDED EVENT"+i);
            }
        }
        catch(FileNotFoundException e){
            System.out.println("ERROR THROWN, COULD NOT COMPLETE ADDING EVENTS.");
            return;
        }
        System.out.println("SUCCESFULLY ADDED ALL EVENTS");
    }
    public static void loadCutscenes(){
        try{
            File ev1 = new File("baseData/CUTSCENE1.txt");
            Scanner ev1in = new Scanner(ev1);
            int ev1n = ev1in.nextInt();
            for(int i = 0; i < ev1n; i++){
                Cutscene temp = new Cutscene(Dilet);
                ArrayList<Event> events = new ArrayList<>();
                ArrayList<Integer> times = new ArrayList<>();
                int num = ev1in.nextInt();
                for(int j =0; j < num; j++){
                    events.add(globalEvents.get(ev1in.nextInt()));
                    times.add(ev1in.nextInt());
                }
                for(int k = 0; k < num; k++){
                    temp.addEvent(events.get(k), times.get(k));
                }
                globalEvents.add(temp);
                System.out.println("ADDED EVENT"+i);
            }
        }
        catch(FileNotFoundException e){
            System.out.println("ERROR THROWN, COULD NOT COMPLETE ADDING CUTSCENES.");
            return;
        }
        System.out.println("SUCCESFULLY ADDED ALL CUTSCENES");
    }
    public static void loadQuests(){
        try{
            File ev1 = new File("baseData/QUEST1.txt");
            Scanner ev1in = new Scanner(ev1);
            int ev1n = ev1in.nextInt();
            String chaff = ev1in.nextLine();
            for(int i = 0; i < ev1n; i++) {
                String name = ev1in.nextLine();
                int num = ev1in.nextInt();
                chaff = ev1in.nextLine();
                ArrayList<String> toAdd = new ArrayList<>();
                for (int j = 0; j < num; j++) {
                    toAdd.add(ev1in.nextLine());
                }
                globalQuests.add(new Quest(name) {{
                    for (String s : toAdd) {
                        addStep(s);
                    }
                }});
                for(String s : toAdd){
                    System.out.println(s);
                }
            }
        }
        catch(FileNotFoundException e){
            System.out.println("ERROR THROWN, COULD NOT COMPLETE ADDING QUESTS.");
            return;
        }
        System.out.println("SUCCESFULLY ADDED ALL QUESTS");
    }
    public static void loadItems(){
        try{
            File it1 = new File("baseData/ITEM1.txt");
            Scanner it1in = new Scanner(it1);
            int it1n = it1in.nextInt();
            String chaff = it1in.nextLine();
            for(int i = 0; i < it1n; i++){
                String name = it1in.nextLine();
                String desc = it1in.nextLine();
                globalItems.add(new Item(name, desc));
                System.out.println("ADDING ITEM "+i);
            }
        }
        catch(FileNotFoundException e){
            System.out.println("ERROR THROWN, COULD NOT COMPLETE ADDING ITEMS.");
            return;
        }
        System.out.println("SUCCESFULLY ADDED ALL ITEMS");
    }
    public static void loadDialogue(){
        try{
            File it1 = new File("baseData/DIALOGUE1.txt");
            Scanner it1in = new Scanner(it1);
            int it1n = it1in.nextInt();
            for(int i = 0; i < it1n; i++){
                int cn = it1in.nextInt();
                ArrayList<Integer> ids = new ArrayList<>();
                for(int j = 0; j < cn; j++){
                    ids.add(it1in.nextInt());
                }
                String chaff = it1in.nextLine();
                int cid = it1in.nextInt();
                chaff = it1in.nextLine();
                String label = it1in.nextLine();
                System.out.println(label);
                globalDialogueOptions.add(new DialogueOption(ids, globalEvents.get(cid), label));
                System.out.println("ADDED "+label);
            }
        }
        catch(FileNotFoundException e){
            System.out.println("ERROR THROWN, COULD NOT COMPLETE ADDING DIALOGUES.");
            return;
        }
        System.out.println("SUCCESFULLY ADDED ALL DIALOGUES");
    }
    public static void loadNPC(){
        try{
            File it1 = new File("baseData/NPC1.txt");
            Scanner it1in = new Scanner(it1);
            int it1n = it1in.nextInt();
            for(int i = 0; i < it1n; i++){
                int x1, y1;
                x1 = it1in.nextInt()* resScalar;
                y1 = it1in.nextInt()* resScalar;

                String chaff = it1in.nextLine();
                int firstd = it1in.nextInt();
                chaff = it1in.nextLine();
                String name = it1in.nextLine();
                globalNPCs.add(new NPC(x1, y1 ,0, globalDialogueOptions.get(firstd), name));
                System.out.println("ADDED "+i);
            }
        }
        catch(FileNotFoundException e){
            System.out.println("ERROR THROWN, COULD NOT COMPLETE ADDING NPC.");
            return;
        }
        System.out.println("SUCCESFULLY ADDED ALL NPC");
    }
    public static void loadChest(){
        try{
            File it1 = new File("baseData/CHEST1.txt");
            Scanner it1in = new Scanner(it1);
            int it1n = it1in.nextInt();
            for(int i = 0; i < it1n; i++){
                int x1, y1;
                ArrayList<Item> cur = new ArrayList<>();
                x1 = it1in.nextInt()* resScalar;
                y1 = it1in.nextInt()* resScalar;
                String chaff = it1in.nextLine();
               int numItems = it1in.nextInt();
               chaff = it1in.nextLine();
               for(int j = 0; j < numItems; j++){
                   cur.add(globalItems.get(it1in.nextInt()));
               }
               globalChests.add(new Chest(x1,y1,cur,Dilet));
               System.out.println("ADDED "+i);
            }
        }
        catch(FileNotFoundException e){
            System.out.println("ERROR THROWN, COULD NOT COMPLETE ADDING CHESTS.");
            return;
        }
        System.out.println("SUCCESFULLY ADDED ALL CHESTS");
    }
    public static void loadInteractable(){
        try{
            File it1 = new File("baseData/INTERACT.txt");
            Scanner it1in = new Scanner(it1);
            int it1n = it1in.nextInt();
            for(int i = 0; i < it1n; i++){
                int x1, y1;
                x1 = it1in.nextInt()* resScalar;
                y1 = it1in.nextInt()* resScalar;
                String chaff = it1in.nextLine();
                int firstd = it1in.nextInt();
                globalInteractives.add(new InteractableObject(x1,y1, firstd));
                System.out.println("ADDED "+i);
            }
        }
        catch(FileNotFoundException e){
            System.out.println("ERROR THROWN, COULD NOT COMPLETE ADDING INTERACTABLE.");
            return;
        }
        System.out.println("SUCCESFULLY ADDED ALL INTERACTABLE");
    }
    public static void loadWorldObj(){
        try{
            File it1 = new File("baseData/OBSTACLE1.txt");
            Scanner it1in = new Scanner(it1);
            int it1n = it1in.nextInt();
            String chaff = it1in.nextLine();
            for(int i = 0; i < it1n; i++){
                    int x, y;
                    x = it1in.nextInt()* resScalar;
                    y = it1in.nextInt()* resScalar;
                    globalObjects.add(new WorldObject(x, y));
                    System.out.println(x+" "+y);
                System.out.println("ADDED "+i);
            }
        }
        catch(FileNotFoundException e){
            System.out.println("ERROR THROWN, COULD NOT COMPLETE ADDING WORLDOBJ.");
            return;
        }
        System.out.println("SUCCESFULLY ADDED ALL WORLDOBJ");
    }
    public static void loadZone(){
        try{
            File it1 = new File("baseData/ZONE1.txt");
            Scanner it1in = new Scanner(it1);
            int it1n = it1in.nextInt();
            for(int i = 0; i < it1n; i++){
                int n, e, s, w;
                n = it1in.nextInt();
                e = it1in.nextInt();
                s = it1in.nextInt();
                w = it1in.nextInt();
                Zone temp = new Zone(n,e,s,w);
                int woc = it1in.nextInt();
                for(int j = 0; j < woc; j++){
                    temp.addWorldObject(globalObjects.get(it1in.nextInt()), it1in.nextInt() * resScalar, it1in.nextInt() * resScalar);
                    System.out.println("ADDED "+j);
                }

                int ioc = it1in.nextInt();
                for(int j = 0; j < ioc; j++){
                    temp.addInteractable(globalInteractives.get(it1in.nextInt()), it1in.nextInt() * resScalar, it1in.nextInt()* resScalar);
                    System.out.println("ADDED "+j);
                }

                int cc = it1in.nextInt();
                for(int j = 0; j < cc; j++){
                    temp.addChest(globalChests.get(it1in.nextInt()), it1in.nextInt()* resScalar, it1in.nextInt()* resScalar);
                    System.out.println("ADDED "+j);
                }

                int npcc = it1in.nextInt();
                for(int j = 0; j < npcc; j++){
                    temp.addNPCs(globalNPCs.get(it1in.nextInt()), it1in.nextInt()* resScalar, it1in.nextInt()* resScalar);
                    System.out.println("ADDED "+j);
                }
                globalZones.add(temp);
            }
        }
        catch(FileNotFoundException e){
            System.out.println("ERROR THROWN, COULD NOT COMPLETE ADDING ZONE.");
            return;
        }
        System.out.println("SUCCESFULLY ADDED ALL ZONE");
    }
    public static void main(String[] args) {
        cutSceneInd = 0;
        newStart = 0;
        load();
        PApplet.main("Main");
    }
    public void settings(){
        size(640* resScalar,360* resScalar);
    }
    public void setup(){
        frameRate(60);
    }
    public void draw(){
        elapsedTime = millis();
        inputProcess();
        if(curState != 1){
            up = false;
            down = false;
            left = false;
            right = false;
        }
        if(curState == 1 || curState == 3 || curState == 4 || curState == 7){
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
            if(millis()-newStart < globalEvents.get(curCutscene).getDelays().get(cutSceneInd) || !globalEvents.get(curCutscene).getSeq().get(cutSceneInd).done()){
                text(globalEvents.get(curCutscene).getSeq().get(cutSceneInd).message(),15 * resScalar,15 * resScalar);
            }
            else{
                cutSceneInd++;
                newStart = millis();
                if(cutSceneInd == globalEvents.get(curCutscene).getDelays().size()){
                    curState = 1;
                }
            }
        }
        if(curState == 3){
            textSize(10 * resScalar);
            text(globalEvents.get(curInteractive.msg()).message(),15 * resScalar,15 * resScalar);
        }
        if(curState == 4){
            fill(0,0,0);
            textSize(10 * resScalar);
            Event i = curDialogueOption.returnTrigger();
            if(i.returnQuestID() != -1) {
                globalQuests.get(i.returnQuestID()).progress(i.returnQuestStep());
                if(globalQuests.get(i.returnQuestID()).questDone()){
                    questLog.remove(i);
                }
            }

            text(curDialogueOption.use(), 15 * resScalar,15 * resScalar);
            int counter = 0;
            textSize(20 * resScalar);
            for(int co : curDialogueOption.getAdj()){
                fill(255,0,127);
                rect(15 * resScalar,(45 + (15 * counter))* resScalar,100,(55 + (15  * counter))* resScalar);
                fill(0,0,0);
                text(globalDialogueOptions.get(co).returnLabel(),15 * resScalar,(45 + 15 *counter) *  resScalar);
                counter++;
            }
        }
        if(curState == 5){
            chestMenu();
        }
        if(curState == 8){
            inventoryMenu();
        }

    }
    public void drawBanner(){
        //top banner
        fill(225,0,0);
        rect(0,0,640 * resScalar,30 * resScalar);
        //pause button
        fill(0,0,255);
        rect(610 * resScalar,5* resScalar,630* resScalar,25* resScalar);
        textSize(10 * resScalar);
        if(questLog.size() != 0){
            text(questLog.get(0).getName()+": "+questLog.get(0).curStep(), 60 * resScalar,40 * resScalar);
        }
        else{
            text("No active quest", 60 * resScalar, 40 * resScalar);
        }
    }
    public void menu(){
        fill(0,255,255);
        rect(0,30 * resScalar,640 * resScalar,360 * resScalar);
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
            rectMode(CORNERS);
            fill(255,255,255);
            rect(curZone.getZoneWorldObjectCoords(ctr).getX(), curZone.getZoneWorldObjectCoords(ctr).getY() ,curZone.getZoneWorldObjectCoords(ctr).getX()+ obj.getLen(),curZone.getZoneWorldObjectCoords(ctr).getY()+ obj.getHeight());
            ctr++;
        }
        ctr = 0;
        for(InteractableObject obj : curZone.getZoneInteractableObjects()){
            rectMode(CORNERS);
            fill(0,255,255);
            rect(curZone.getZoneInteractableObjectCoords(ctr).getX(), curZone.getZoneInteractableObjectCoords(ctr).getY() ,curZone.getZoneInteractableObjectCoords(ctr).getX()+ obj.getLen(),curZone.getZoneInteractableObjectCoords(ctr).getY()+ obj.getHeight());
            ctr++;
        }
        ctr =0;
        for(NPC obj : curZone.getNPCs()){
            rectMode(CORNERS);
            fill(255,0,255);
            rect(curZone.getZoneNPCCoords(ctr).getX(), curZone.getZoneNPCCoords(ctr).getY() ,curZone.getZoneNPCCoords(ctr).getX()+ obj.getLen(),curZone.getZoneNPCCoords(ctr).getY()+ obj.getHeight());
            ctr++;
        }
        ctr=0;
        for(Chest obj : curZone.getZoneChests()){
            rectMode(CORNERS);
            fill(255,255,0);
            rect(curZone.getZoneChestCoords(ctr).getX(), curZone.getZoneChestCoords(ctr).getY() ,curZone.getZoneChestCoords(ctr).getX()+ obj.getLen(),curZone.getZoneChestCoords(ctr).getY()+ obj.getHeight());
            ctr++;
        }
    }
    public void drawPlayer() {
        fill(0, 230, 172);
        rectMode(CENTER);
        rect(Dilet.getX(), Dilet.getY(), 30* resScalar, 30* resScalar);
    }
    public void inputProcess(){
        if(curState == 1){
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
        for(InteractableObject i : curZone.getZoneInteractableObjects()){
            System.out.println(i.toString());
        }
        if(curState == 1){
            if(mouseX >= 610* resScalar && mouseX <= 630* resScalar && mouseY >= 5* resScalar && mouseY <= 25* resScalar){
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
                            if(globalEvents.get(i.msg()).isCutscene()){
                                curState = 7;
                                newStart = millis();
                                System.out.println("START: "+millis());
                                cutSceneInd = 0;
                                curCutscene = i.msg();
                                for(Integer time : globalEvents.get(curCutscene).getDelays()){
                                    System.out.println(time);
                                }
                            }
                            else{
                                if(globalEvents.get(i.msg()).returnQuestID() != -1){
                                    globalQuests.get(globalEvents.get(i.msg()).returnQuestID()).progress(globalEvents.get(i.msg()).returnQuestStep());
                                    if(globalQuests.get(globalEvents.get(i.msg()).returnQuestID()).questDone()){
                                        questLog.remove(globalQuests.get(globalEvents.get(i.msg()).returnQuestID()));
                                    }
                                }
                                curState = 3;
                                curInteractive = i;
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
                        }
                    }
                }
                ctr++;
            }
            ctr =0;
            for(Chest c : curZone.getZoneChests()){
                if(mouseX >= curZone.getZoneChestCoords(ctr).getX() && mouseX <= curZone.getZoneChestCoords(ctr).getX() + c.getLen() && mouseY >= curZone.getZoneChestCoords(ctr).getY() && mouseY <= curZone.getZoneChestCoords(ctr).getY() + c.getHeight()){
                    if(Math.abs(Dilet.getX() - (curZone.getZoneChestCoords(ctr).getX()+ (c.getLen()/2))) < 60 * resScalar){
                        if(Math.abs(Dilet.getY() - (curZone.getZoneChestCoords(ctr).getY()+ (c.getHeight()))) < 60 * resScalar){
                            System.out.println("CHEST ACTIVE");
                            curState = 5;
                            curChest = c;
                        }
                    }
                }
                ctr++;
            }
        }
        else if(curState == 2){
            if(mouseX >= 610* resScalar && mouseX <= 630* resScalar && mouseY >= 5* resScalar && mouseY <= 25* resScalar){
                curState = 1;
                System.out.println("UNPAUSED");
            }
        }
        else if(curState == 3){
            globalEvents.get(curInteractive.msg()).reset();
            System.out.println("EXITING INTERACT");//to be replaced with dialogue n stuff
            curState = 1;
        }
        else if(curState == 4){
            if(mouseX >= 15 * resScalar && mouseX <= 100 * resScalar){
                for(int i = 0; i < curDialogueOption.getAdj().size(); i++){
                    if(mouseY >= (45 + (15 * i))* resScalar && mouseY <= (55 + (15 * i))* resScalar){
                        curDialogueOption = globalDialogueOptions.get(curDialogueOption.getAdj().get(i));
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
    }
    public void keyPressed(){
        if(curState == 1){
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
        if(curState == 1){
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

}