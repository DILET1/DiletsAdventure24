import processing.core.*;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.*;
public class Main extends PApplet {
    //all of our fun vars
    static ArrayList<Zone> zoneList = new ArrayList<>();
    static ArrayList<Event> events = new ArrayList<>();
    static ArrayList<Item> items = new ArrayList<>();
    static ArrayList<InteractableObject> interactables = new ArrayList<>();
    static ArrayList<WorldObject> objList = new ArrayList<>();
    static ArrayList<DialogueOption> options = new ArrayList<>();
    static ArrayList<Chest> chests = new ArrayList<>();
    static ArrayList<NPC> npcs = new ArrayList<>();
    public static int resScalar = 2;
    static Player Dilet = new Player(resScalar);
    static int currentState = 1; //state -1 is intro cutscene. state 0 is menu. state 1 is in-game. state 2 is pause menu. state 3 is interacting with an object. state 4 is dialogue. state 5 is chest.  more to come
    static Zone curZone = null;
    static InteractableObject curInteract = null;
    static NPC curNPC = null;
    static DialogueOption curDialogue = null;
    static Chest curChest = null;
    static boolean up, left, right, down;
    //TEST AREA
    //END TEST
    public static void load(){
        loadItems();
        loadEvents();
        loadDialogue();
        loadNPC();
        loadChest();
        loadInteractable();
        loadWorldObj();
        loadZone();
        curZone = zoneList.get(0);
    }
    public static void loadEvents(){
        try{
            File ev1 = new File("baseData/EVENT1.txt");
            Scanner ev1in = new Scanner(ev1);
            int ev1n = ev1in.nextInt();
            for(int i = 0; i < ev1n; i++){
                System.out.println("PASS");
                int type = ev1in.nextInt();
                String chaff = ev1in.nextLine();
                String message = ev1in.nextLine();
                String isSilent = ev1in.nextLine();
                if(type == 1){
                    events.add(new Event(message, (Objects.equals(isSilent, "SILENT")), Dilet));
                }
                if(type == 2 || type == 3){
                    Item ta = items.get(ev1in.nextInt());
                    if(type == 2){
                        events.add(new TakeEvent(message, isSilent.equals("SILENT"), Dilet, ta));
                    }
                    else{
                        events.add(new GiveEvent(message, isSilent.equals("SILENT"), Dilet, ta));
                    }
                }
                if(type == 4 || type == 5 || type == 6 || type == 7 || type == 8 || type == 9){
                    int zone = ev1in.nextInt();
                    int ind = ev1in.nextInt();
                    int x = ev1in.nextInt();
                    int y = ev1in.nextInt();
                    if(type == 4){
                        events.add(new RemoveEvent(Dilet, zone, ind, x,y,zoneList, objList));
                    }
                    if(type == 5){
                        events.add(new PlaceEvent(Dilet, zone, ind, x,y,zoneList, objList));
                    }
                    if(type == 6){
                        events.add(new removeInteractable(Dilet, zone, ind, x,y,zoneList, interactables));
                    }
                    if(type == 7){
                        events.add(new placeInteractable(Dilet, zone, ind, x,y,zoneList, interactables));
                    }
                    if(type == 8){
                        events.add(new removeNPC(Dilet, zone, ind, zoneList, npcs));
                    }
                    if(type == 9){
                        events.add(new placeNPC(Dilet, zone, ind, x,y,zoneList, npcs));
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
    public static void loadItems(){
        try{
            File it1 = new File("baseData/ITEM1.txt");
            Scanner it1in = new Scanner(it1);
            int it1n = it1in.nextInt();
            String chaff = it1in.nextLine();
            for(int i = 0; i < it1n; i++){
                String name = it1in.nextLine();
                String desc = it1in.nextLine();
                items.add(new Item(name, desc));
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
                options.add(new DialogueOption(ids, events.get(cid), label));
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
                npcs.add(new NPC(x1, y1 ,0, options.get(firstd), name));
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
                   cur.add(items.get(it1in.nextInt()));
               }
               chests.add(new Chest(x1,y1,cur,Dilet));
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
                interactables.add(new InteractableObject(x1,y1, firstd));
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
                    objList.add(new WorldObject(x, y));
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
                    temp.addObj(objList.get(it1in.nextInt()), it1in.nextInt() * resScalar, it1in.nextInt() * resScalar);
                    System.out.println("ADDED "+j);
                }

                int ioc = it1in.nextInt();
                for(int j = 0; j < ioc; j++){
                    temp.addInteractables(interactables.get(it1in.nextInt()), it1in.nextInt() * resScalar, it1in.nextInt()* resScalar);
                    System.out.println("ADDED "+j);
                }

                int cc = it1in.nextInt();
                for(int j = 0; j < cc; j++){
                    temp.addChest(chests.get(it1in.nextInt()), it1in.nextInt()* resScalar, it1in.nextInt()* resScalar);
                    System.out.println("ADDED "+j);
                }

                int npcc = it1in.nextInt();
                for(int j = 0; j < npcc; j++){
                    temp.addNPCs(npcs.get(it1in.nextInt()), it1in.nextInt()* resScalar, it1in.nextInt()* resScalar);
                    System.out.println("ADDED "+j);
                }
                zoneList.add(temp);
            }
        }
        catch(FileNotFoundException e){
            System.out.println("ERROR THROWN, COULD NOT COMPLETE ADDING ZONE.");
            return;
        }
        System.out.println("SUCCESFULLY ADDED ALL ZONE");
    }
    public static void main(String[] args) {
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
        inputProcess();
        if(currentState == 1 || currentState == 3 || currentState == 4){
            background(0,255,0);
            noStroke();
            fill(255,255,255);
            drawPlayer();
            drawZone();
        }
        if(currentState == 2){
            menu();
        }
        drawBanner();
        if(currentState == 3){
            textSize(10 * resScalar);
            text(events.get(curInteract.msg()).message(),15 * resScalar,15 * resScalar);
        }
        if(currentState == 4){
            fill(0,0,0);
            textSize(10 * resScalar);
            text(curDialogue.use(), 15 * resScalar,15 * resScalar);
            int counter = 0;
            textSize(10 * resScalar);
            for(int co : curDialogue.getAdj()){
                fill(255,0,127);
                rect(15 * resScalar,(45 + (15 * counter))* resScalar,100,(55 + (15  * counter))* resScalar);
                fill(0,0,0);
                text(options.get(co).returnLabel(),15 * resScalar,(45 + 15 *counter) *  resScalar);
                counter++;
            }
        }
        if(currentState == 5){
            chestMenu();
        }
    }
    public void drawBanner(){
        //top banner
        fill(225,0,0);
        rect(0,0,640 * resScalar,30 * resScalar);
        //pause button
        fill(0,255,0);
        rect(610 * resScalar,5* resScalar,630* resScalar,25* resScalar);
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
    public void drawZone(){
        int ctr =0;
        for(WorldObject obj : curZone.getObstacles()){
            rectMode(CORNERS);
            fill(255,255,255);
            rect(curZone.getCoord(ctr).getX(), curZone.getCoord(ctr).getY() ,curZone.getCoord(ctr).getX()+ obj.getLen(),curZone.getCoord(ctr).getY()+ obj.getHeight());
            ctr++;
        }
        ctr = 0;
        for(InteractableObject obj : curZone.getInteractables()){
            rectMode(CORNERS);
            fill(0,255,255);
            rect(curZone.getIcoord(ctr).getX(), curZone.getIcoord(ctr).getY() ,curZone.getIcoord(ctr).getX()+ obj.getLen(),curZone.getIcoord(ctr).getY()+ obj.getHeight());
            ctr++;
        }
        ctr =0;
        for(NPC obj : curZone.getNPCs()){
            rectMode(CORNERS);
            fill(255,0,255);
            rect(curZone.getnCoord(ctr).getX(), curZone.getnCoord(ctr).getY() ,curZone.getnCoord(ctr).getX()+ obj.getLen(),curZone.getnCoord(ctr).getY()+ obj.getHeight());
            ctr++;
        }
        ctr=0;
        for(Chest obj : curZone.getChests()){
            rectMode(CORNERS);
            fill(255,255,0);
            rect(curZone.getCcoord(ctr).getX(), curZone.getCcoord(ctr).getY() ,curZone.getCcoord(ctr).getX()+ obj.getLen(),curZone.getCcoord(ctr).getY()+ obj.getHeight());
            ctr++;
        }
    }
    public void drawPlayer() {
        fill(0, 230, 172);
        rectMode(CENTER);
        rect(Dilet.getX(), Dilet.getY(), 30* resScalar, 30* resScalar);
    }
    public void inputProcess(){
        if(currentState == 1){
            if (up) {
                boolean uflag = true;
                int ctr = 0;
                for(WorldObject w : curZone.getObstacles()){
                    if(!Dilet.canUp(w,curZone.getCoord(ctr)) ){
                        uflag = false;
                    }
                    ctr++;
                }
                ctr = 0;
                for(Chest w : curZone.getChests()){
                    if(!Dilet.canUp(w, curZone.getCcoord(ctr))){
                        uflag = false;
                    }
                    ctr++;
                }
                ctr= 0;
                for(NPC w : curZone.getNPCs()){
                    if(!Dilet.canUp(w, curZone.getnCoord(ctr))){
                        uflag = false;
                    }
                    ctr++;
                }
                ctr = 0;
                for(InteractableObject w : curZone.getInteractables()){
                    if(!Dilet.canUp(w, curZone.getIcoord(ctr))){
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
                for(WorldObject w : curZone.getObstacles()){
                    if(!Dilet.canLeft(w,curZone.getCoord(ctr)) ){
                        lflag = false;
                    }
                    ctr++;
                }
                ctr = 0;
                for(Chest w : curZone.getChests()){
                    if(!Dilet.canLeft(w, curZone.getCcoord(ctr))){
                        lflag = false;
                    }
                    ctr++;
                }
                ctr= 0;
                for(NPC w : curZone.getNPCs()){
                    if(!Dilet.canLeft(w, curZone.getnCoord(ctr))){
                        lflag = false;
                    }
                    ctr++;
                }
                ctr = 0;
                for(InteractableObject w : curZone.getInteractables()){
                    if(!Dilet.canLeft(w, curZone.getIcoord(ctr))){
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
                for(WorldObject w : curZone.getObstacles()){
                    if(!Dilet.canRight(w,curZone.getCoord(ctr)) ){
                        rflag = false;
                    }
                    ctr++;
                }
                ctr = 0;
                for(Chest w : curZone.getChests()){
                    if(!Dilet.canRight(w, curZone.getCcoord(ctr))){
                        rflag = false;
                    }
                    ctr++;
                }
                ctr= 0;
                for(NPC w : curZone.getNPCs()){
                    if(!Dilet.canRight(w, curZone.getnCoord(ctr))){
                        rflag = false;
                    }
                    ctr++;
                }
                ctr = 0;
                for(InteractableObject w : curZone.getInteractables()){
                    if(!Dilet.canRight(w, curZone.getIcoord(ctr))){
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
                for(WorldObject w : curZone.getObstacles()){
                    if(!Dilet.canDown(w,curZone.getCoord(ctr)) ){
                        dflag = false;
                    }
                    ctr++;
                }
                ctr = 0;
                for(Chest w : curZone.getChests()){
                    if(!Dilet.canDown(w, curZone.getCcoord(ctr))){
                        dflag = false;
                    }
                    ctr++;
                }
                ctr= 0;
                for(NPC w : curZone.getNPCs()){
                    if(!Dilet.canDown(w, curZone.getnCoord(ctr))){
                        dflag = false;
                    }
                    ctr++;
                }
                ctr = 0;
                for(InteractableObject w : curZone.getInteractables()){
                    if(!Dilet.canDown(w, curZone.getIcoord(ctr))){
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
        System.out.println(curZone.getObstacles().size());
        for(WorldObject w : curZone.getObstacles()){
            System.out.println(w.toString());
        }
        System.out.println(curZone.getInteractables().size());
        for(InteractableObject i : curZone.getInteractables()){
            System.out.println(i.toString());
        }
        if(currentState == 1){
            if(mouseX >= 610* resScalar && mouseX <= 630* resScalar && mouseY >= 5* resScalar && mouseY <= 25* resScalar){
                currentState = 2;
                System.out.println("PAUSED");
                for(Item i : Dilet.getInventory()){
                    System.out.println(i.toString());
                }
            }
            int ctr = 0;
            for(InteractableObject i : curZone.getInteractables()){
                if(mouseX >= curZone.getIcoord(ctr).getX() && mouseX <= curZone.getIcoord(ctr).getX() + i.getLen() && mouseY >= curZone.getIcoord(ctr).getY() && mouseY <= curZone.getIcoord(ctr).getY() + i.getHeight()){
                    if(Math.abs(Dilet.getX() - (curZone.getIcoord(ctr).getX()+ (i.getLen()/2))) < 60 * resScalar){
                        if(Math.abs(Dilet.getY() - (curZone.getIcoord(ctr).getY()+ (i.getHeight()))) < 60 * resScalar){
                            currentState = 3;
                            curInteract = i;
                        }
                    }
                }
                ctr++;
            }
            ctr = 0;
            for(NPC n : curZone.getNPCs()){
                if(mouseX >= curZone.getnCoord(ctr).getX() && mouseX <= curZone.getnCoord(ctr).getX() + n.getLen() && mouseY >= curZone.getnCoord(ctr).getY() && mouseY <= curZone.getnCoord(ctr).getY() + n.getHeight()){
                    if(Math.abs(Dilet.getX() - (curZone.getnCoord(ctr).getX()+ (n.getLen()/2))) < 60 * resScalar){
                        if(Math.abs(Dilet.getY() - (curZone.getnCoord(ctr).getY()+ (n.getHeight()))) < 60 * resScalar){
                            System.out.println("NPC ACTIVE");
                            currentState = 4;
                            curNPC = n;
                            curDialogue = n.talk();
                        }
                    }
                }
                ctr++;
            }
            ctr =0;
            for(Chest c : curZone.getChests()){
                if(mouseX >= curZone.getCcoord(ctr).getX() && mouseX <= curZone.getCcoord(ctr).getX() + c.getLen() && mouseY >= curZone.getCcoord(ctr).getY() && mouseY <= curZone.getCcoord(ctr).getY() + c.getHeight()){
                    if(Math.abs(Dilet.getX() - (curZone.getCcoord(ctr).getX()+ (c.getLen()/2))) < 60 * resScalar){
                        if(Math.abs(Dilet.getY() - (curZone.getCcoord(ctr).getY()+ (c.getHeight()))) < 60 * resScalar){
                            System.out.println("CHEST ACTIVE");
                            currentState = 5;
                            curChest = c;
                        }
                    }
                }
                ctr++;
            }
        }
        else if(currentState == 2){
            if(mouseX >= 610* resScalar && mouseX <= 630* resScalar && mouseY >= 5* resScalar && mouseY <= 25* resScalar){
                currentState = 1;
                System.out.println("UNPAUSED");
            }
        }
        else if(currentState == 3){
            events.get(curInteract.msg()).reset();
            System.out.println("EXITING INTERACT");//to be replaced with dialogue n stuff
            currentState = 1;
        }
        else if(currentState == 4){
            if(mouseX >= 15 * resScalar && mouseX <= 100 * resScalar){
                for(int i = 0; i < curDialogue.getAdj().size(); i++){
                    if(mouseY >= (45 + (15 * i))* resScalar && mouseY <= (55 + (15 * i))* resScalar){
                        curDialogue = options.get(curDialogue.getAdj().get(i));
                        return;
                    }
                }
            }
            currentState = 1;
            System.out.println("Exited dialogue");

        }
        else if(currentState == 5){
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
                currentState = 1;
            }
        }
    }
    public void keyPressed(){
        if(currentState == 1){
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
        }
    }
    public void keyReleased(){
        if(currentState == 1){
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