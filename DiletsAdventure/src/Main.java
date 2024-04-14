import processing.core.*;
import java.util.*;
public class Main extends PApplet{
//    static ArrayList<Zone> zoneList = new ArrayList<>();
//    static ArrayList<WorldObject> objList = new ArrayList<>();
    public static int resScalar = 2;
    static Player Dilet = new Player(resScalar);
    int currentState = 1; //state -1 is intro cutscene. state 0 is menu. state 1 is in-game. state 2 is pause menu. state 3 is interacting with an object. state 4 is dialogue. state 5 is chest.  more to come
    static Zone startArea = new Zone("Startarea");
    static Zone curZone = startArea;
    static InteractableObject curInteract = null;
    static NPC curNPC = null;
    static DialogueOption curDialogue = null;
    static Chest curChest = null;
    //TEST AREA
    static WorldObject load1 = new WorldObject(400 * resScalar, 200 * resScalar, 410 * resScalar, 210 * resScalar);
    static WorldObject load2 = new WorldObject(410 * resScalar, 210 * resScalar, 420 * resScalar, 220 * resScalar);
    static WorldObject load3 = new WorldObject(420 * resScalar, 220 * resScalar, 430 * resScalar, 230 * resScalar);
    static WorldObject load4 = new WorldObject(430 * resScalar, 230 * resScalar, 440 * resScalar, 240 * resScalar);
    static InteractableObject tester = new InteractableObject(320 * resScalar, 320 * resScalar, 340 * resScalar, 340 * resScalar, "FISHY NOMM");
    static Event sayhi = new Event("hi", false, Dilet);
    static Event saybye = new Event("bye", false, Dilet);
    static Event saysup = new Event("sup", false, Dilet);
    static DialogueOption d4 = new DialogueOption(new ArrayList<>(), new Event("Loaded", false, Dilet), "It works");
    static DialogueOption d5 = new DialogueOption(new ArrayList<>(), new Event("Loaded2", false, Dilet), "It works2");
    static DialogueOption d2 = new DialogueOption(new ArrayList<>(){{add(d4);add(d5);}}, saybye, "Say Goodbye.");
    static DialogueOption d3 = new DialogueOption(new ArrayList<>(), saysup, "Say Sup.");
    static DialogueOption d1 = new DialogueOption(new ArrayList<>(){{add(d2);add(d3);}}, sayhi, "Say Hi.");
    static Chest testChest = new Chest(200 * resScalar,200* resScalar,225* resScalar,225* resScalar, new ArrayList<Item>(){{add(new Item("Fish", "very cool"));}}, Dilet);
    static NPC testMan = new NPC(50 * resScalar, 50* resScalar, 100* resScalar, 100* resScalar,"fish" ,d1, "tester");

    //END TEST
    public static void loadAreas(){
       curZone.addObj(tester);
       curZone.addObj(load1);
        curZone.addObj(load2);
        curZone.addObj(load3);
        curZone.addObj(load4);
       curZone.addInteractables(tester);
       curZone.addNPCs(testMan);
       curZone.addObj(testMan);
       curZone.addObj(testChest);
       curZone.addChest(testChest);
    }
    boolean up, left, right, down;
    public static void main(String[] args)
    {
        loadAreas();
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
            text(curInteract.msg(),15 * resScalar,15 * resScalar);
        }
        if(currentState == 4){
            fill(0,0,0);
            textSize(10 * resScalar);
            text(curDialogue.use(), 15 * resScalar,15 * resScalar);
            int counter = 0;
            textSize(10 * resScalar);
            for(DialogueOption co : curDialogue.getAdj()){
                fill(255,0,127);
                rect(15 * resScalar,(45 + (15 * counter))* resScalar,100,(55 + (15  * counter))* resScalar);
                fill(0,0,0);
                text(co.returnLabel(),15 * resScalar,(45 + 15 *counter) *  resScalar);
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
            rect(obj.getLowx(), obj.getLowy(), obj.getHix(),  obj.getHiy());
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
                        curDialogue = curDialogue.getAdj().get(i);
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