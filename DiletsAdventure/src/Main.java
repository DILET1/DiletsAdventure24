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
                    if(type == 4){
                        events.add(new RemoveEvent(Dilet, zone, ind, zoneList, objList));
                    }
                    if(type == 5){
                        events.add(new PlaceEvent(Dilet, zone, ind, zoneList, objList));
                    }
                    if(type == 6){
                        events.add(new removeInteractable(Dilet, zone, ind, zoneList, objList, interactables));
                    }
                    if(type == 7){
                        events.add(new placeInteractable(Dilet, zone, ind, zoneList, objList, interactables));
                    }
                    if(type == 8){
                        events.add(new removeNPC(Dilet, zone, ind, zoneList, objList, npcs));
                    }
                    if(type == 9){
                        events.add(new placeNPC(Dilet, zone, ind, zoneList, objList, npcs));
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
                int x1, x2, y1, y2;
                x1 = it1in.nextInt()* resScalar;
                y1 = it1in.nextInt()* resScalar;
                x2 = it1in.nextInt()* resScalar;
                y2 = it1in.nextInt()* resScalar;
                String chaff = it1in.nextLine();
                int firstd = it1in.nextInt();
                chaff = it1in.nextLine();
                String name = it1in.nextLine();
                npcs.add(new NPC(x1, y1, x2, y2,0, options.get(firstd), name));
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
                int x1, x2, y1, y2;
                ArrayList<Item> cur = new ArrayList<>();
                x1 = it1in.nextInt()* resScalar;
                y1 = it1in.nextInt()* resScalar;
                x2 = it1in.nextInt()* resScalar;
                y2 = it1in.nextInt()* resScalar;
                String chaff = it1in.nextLine();
               int numItems = it1in.nextInt();
               chaff = it1in.nextLine();
               for(int j = 0; j < numItems; j++){
                   cur.add(items.get(it1in.nextInt()));
               }
               chests.add(new Chest(x1,y1,x2,y2,cur,Dilet));
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
                int x1, x2, y1, y2;
                x1 = it1in.nextInt()* resScalar;
                y1 = it1in.nextInt()* resScalar;
                x2 = it1in.nextInt()* resScalar;
                y2 = it1in.nextInt()* resScalar;
                String chaff = it1in.nextLine();
                int firstd = it1in.nextInt();
                interactables.add(new InteractableObject(x1,y1,x2,y2, firstd));
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
                int type = it1in.nextInt();
                System.out.println(type);
                chaff = it1in.nextLine();
                if(type == 0){
                    int x1, x2, y1, y2;
                    x1 = it1in.nextInt()* resScalar;
                    y1 = it1in.nextInt()* resScalar;
                    x2 = it1in.nextInt()* resScalar;
                    y2 = it1in.nextInt()* resScalar;
                    objList.add(new WorldObject(x1,y1,x2,y2));
                }
                else{
                    int ind = it1in.nextInt();
                    if(type == 1){
                        objList.add(interactables.get(ind));
                    }
                    else if(type == 2){
                        objList.add(chests.get(ind));
                    }
                    else if(type == 3){
                        objList.add(npcs.get(ind));
                    }
                }
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
                    temp.addObj(objList.get(it1in.nextInt()));
                    System.out.println("ADDED "+j);
                }

                int ioc = it1in.nextInt();
                for(int j = 0; j < ioc; j++){
                    temp.addInteractables(interactables.get(it1in.nextInt()));
                    System.out.println("ADDED "+j);
                }

                int cc = it1in.nextInt();
                for(int j = 0; j < cc; j++){
                    temp.addChest(chests.get(it1in.nextInt()));
                    System.out.println("ADDED "+j);
                }

                int npcc = it1in.nextInt();
                for(int j = 0; j < npcc; j++){
                    temp.addNPCs(npcs.get(it1in.nextInt()));
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
        for(WorldObject obj : curZone.getObstacles()){
            rectMode(CORNERS);
            fill(255,255,255);
            rect(obj.getLowx(), obj.getLowy() , obj.getHix(),  obj.getHiy());
        }
        for(InteractableObject io : curZone.getInteractables()){
            rectMode(CORNERS);
            fill(255,255,255);
            rect(io.getLowx(), io.getLowy(), io.getHix(),  io.getHiy());
        }
        for(NPC n : curZone.getNPCs()){
            rectMode(CORNERS);
            fill(255,255,255);
            rect(n.getLowx(), n.getLowy(), n.getHix(),  n.getHiy());
        }
        for(Chest c : curZone.getChests()){
            rectMode(CORNERS);
            rect(c.getLowx(), c.getLowy(), c.getHix(), c.getHiy());
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
                for(WorldObject w : curZone.getObstacles()){
                    if(!Dilet.canUp(w)){
                        uflag = false;
                    }
                }
                if(uflag){
                    Dilet.moveY(-5 * resScalar);
                }
            }
            if (left) {
                boolean lflag = true;
                for(WorldObject w : curZone.getObstacles()){
                    if(!Dilet.canLeft(w)){
                        lflag = false;
                    }
                }
                if(lflag){
                    Dilet.moveX(-5* resScalar);
                }

            }
            if (right) {
                boolean rflag = true;
                for(WorldObject w : curZone.getObstacles()){
                    if(!Dilet.canRight(w)){
                        rflag = false;
                    }
                }
                if(rflag){
                    Dilet.moveX(5* resScalar);
                }
            }
            if (down) {
                boolean dflag = true;
                for(WorldObject w : curZone.getObstacles()){
                    if(!Dilet.canDown(w)){
                        dflag = false;
                    }
                }
                if(dflag){
                    Dilet.moveY(5* resScalar);
                }
            }

        }
    }
    public void mouseClicked() {
        System.out.println(mouseX+" "+mouseY);
        if(currentState == 1){
            if(mouseX >= 610* resScalar && mouseX <= 630* resScalar && mouseY >= 5* resScalar && mouseY <= 25* resScalar){
                currentState = 2;
                System.out.println("PAUSED");
                for(Item i : Dilet.getInventory()){
                    System.out.println(i.toString());
                }
            }

            for(InteractableObject i : curZone.getInteractables()){
                if(mouseX >= i.getLowx() && mouseX <= i.getHix() && mouseY >= i.getLowy() && mouseY <= i.getHiy()){
                    if(Math.abs(Dilet.getX() - (i.getLowx()+ (i.getHix() - i.getLowx())/2)) < 60 * resScalar){
                        if(Math.abs(Dilet.getY() - (i.getLowy()+ (i.getHiy() - i.getLowy())/2)) < 60 * resScalar){
                            currentState = 3;
                            curInteract = i;
                        }
                    }
                }
            }

            for(NPC n : curZone.getNPCs()){
                if(mouseX >= n.getLowx() && mouseX <= n.getHix() && mouseY >= n.getLowy() && mouseY <= n.getHiy()){
                    if(Math.abs(Dilet.getX() - (n.getLowx()+ (n.getHix() - n.getLowx())/2)) < 60 * resScalar){
                        if(Math.abs(Dilet.getY() - (n.getLowy()+ (n.getHiy() - n.getLowy())/2)) < 60 * resScalar){
                            System.out.println("NPC ACTIVE");
                            currentState = 4;
                            curNPC = n;
                            curDialogue = n.talk();
                        }
                    }
                }
            }
            for(Chest c : curZone.getChests()){
                if(mouseX >= c.getLowx() && mouseX <= c.getHix() && mouseY >= c.getLowy() && mouseY <= c.getHiy()){
                    if(Math.abs(Dilet.getX() - (c.getLowx()+ (c.getHix() - c.getLowx())/2)) < 60 * resScalar){
                        if(Math.abs(Dilet.getY() - (c.getLowy()+ (c.getHiy() - c.getLowy())/2)) < 60 * resScalar){
                            System.out.println("CHEST ACTIVE");
                            currentState = 5;
                            curChest = c;
                        }
                    }
                }
            }
        }
        else if(currentState == 2){
            if(mouseX >= 610* resScalar && mouseX <= 630* resScalar && mouseY >= 5* resScalar && mouseY <= 25* resScalar){
                currentState = 1;
                System.out.println("UNPAUSED");
            }
        }
        else if(currentState == 3){
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