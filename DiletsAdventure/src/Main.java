import processing.core.*;
import java.util.*;
public class Main extends PApplet{
    static ArrayList<Zone> zoneList = new ArrayList<>();
    static ArrayList<WorldObject> objList = new ArrayList<>();
    Player Dilet = new Player();
    static Zone startArea = new Zone("Startarea");
    Zone curZone = startArea;
    public static void loadAreas(){

    }
    boolean up, left, right, down;
    public static void main(String[] args)
    {
        loadAreas();
        PApplet.main("Main");
    }
    public void settings(){
        size(400,400);
    }
    public void draw(){
        background(0,0,0);
        noStroke();
        drawPlayer();
        drawZone();
    }
    public void drawZone(){
        for(WorldObject obj : curZone.getObstacles()){
            rectMode(CORNERS);
            fill(255,255,255);
            rect(obj.getLowx(), obj.getLowy(), obj.getHix(),  obj.getHiy());
        }

    }
    public void drawPlayer() {
        fill(0, 230, 172);
        rectMode(CENTER);
        rect(Dilet.getX(), Dilet.getY(), 30, 30);
        inputProcess();
    }
    public void inputProcess(){
        if (up) {
            boolean uflag = true;
            for(WorldObject w : curZone.getObstacles()){
                if(!Dilet.canUp(w)){
                    uflag = false;
                }
            }
            if(uflag){
                Dilet.moveY(-5);
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
                Dilet.moveX(-5);
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
                Dilet.moveX(5);
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
                Dilet.moveY(5);
            }
        }
    }
    public void keyPressed(){
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
    public void keyReleased(){
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