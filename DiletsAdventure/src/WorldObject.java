import processing.core.PImage;

import java.util.ArrayList;

public class WorldObject {
    private int dx, dy; //dx = width dy = height
    private ArrayList<PImage> sprites = new ArrayList<>();
    public WorldObject(int dx, int dy){
        this.dx = dx;
        this.dy = dy;
    }
    public int getLen(){
        return this.dx;
    }
    public int getHeight(){
        return this.dy;
    }
    public String toString(){
        return(this.dx + " "+ this.dy+" ");
    }
}
