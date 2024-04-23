import processing.core.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.*;
public class Main extends PApplet {
    //all of our fun vars
    static ArrayList<Zone> globalZones = new ArrayList<>();
    static ArrayList<Item> globalItems = new ArrayList<>();
    static ArrayList<WorldObject> globalObjects = new ArrayList<>();
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
    static int cutSceneInd;//ditto above
    static boolean up, left, right, down;
    public static void load(){
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
    public static void loadZone(String directory, int ind){
        loadEvents(directory, ind);
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
                if(type > 1 && type < 8){
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
                if(type > 7 && type < 11){
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
                text(curZone.getDialogue(co, curNPC.getIndex()).returnLabel(),15 * resScalar,(45 + 15 *counter) *  resScalar);
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
        //pause button
        fill(0,0,255);
        rect(610 * resScalar,5* resScalar,630* resScalar,25* resScalar);
        textSize(10 * resScalar);
        if(!questLog.isEmpty()){
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
            fill(255,255,255);
            rectMode(CORNERS);
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
            curZone.getEvent(curInteractive.msg()).reset();
            System.out.println("EXITING INTERACT");//to be replaced with dialogue n stuff
            curState = 1;
        }
        else if(curState == 4){
            if(mouseX >= 15 * resScalar && mouseX <= 100 * resScalar){
                for(int i = 0; i < curDialogueOption.getAdj().size(); i++){
                    if(mouseY >= (45 + (15 * i))* resScalar && mouseY <= (55 + (15 * i))* resScalar){
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