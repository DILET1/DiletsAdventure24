import processing.core.*;
import java.util.*;
public class Main extends PApplet{
    WorldObject w = new WorldObject(200,200,300,300);
    Player Dilet = new Player();
    Zone startArea = new Zone("Startarea");
    Zone curZone = startArea;
    public void loadAreas(){
        startArea.addObj(w);
    }
    boolean up, left, right, down;
    public static void main(String[] args)
    {
        PApplet.main("Main");
    }
    public void settings(){
        size(400,400);
    }
    public void draw(){
        background(0,0,0);
        noStroke();
        drawPlayer();
    }
    public void drawZone(){
        for(WorldObject obj : curZone.getObstacles()){
            rectMode(CORNERS);
            fill(255,255,255);
            
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

            Dilet.moveY(-5);
        }
        if (left) {
            Dilet.moveX(-5);
        }
        if (right) {
            Dilet.moveX(5);
        }
        if (down) {
            Dilet.moveY(5);
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