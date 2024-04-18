import java.util.*;
import processing.core.PApplet;
public class Main extends PApplet {
    public static ArrayList<DisplayObject> objs = new ArrayList<>();
    public static void main(String[] args) {
        System.out.println("Welcome to the v0.0.1 of the level editor for Dilet's Adventure. Compatible version of Dilet's Adventure is v0.0.3.1.");
        System.out.println("If you'd like to preload data, place the data files inside the preload folder (This function is currently not implement).");
        System.out.println("The editor is currently text only.");
        System.out.println("Once done with the program, the files will be in the output folder.");
        PApplet.main("Main");
        int choice = 0;
        Scanner input = new Scanner(System.in);
        while(choice != -1){
            System.out.println("1 to add WorldObject, 2 to add Interactable, 3 to add NPC, 4 to add a chest, 5 to add an item, 6 to add an event, 7 to add a dialogue option, 8 to add a cutscene, 9 to add a quest.");
            choice = input.nextInt();
            if(choice == -1){
                System.out.println("Your data:");
                System.out.println(objs.size());
                for(DisplayObject d : objs){
                    System.out.println(d.getLen() +" "+ d.getHeight());
                }
                System.out.println("Your zone:");
                System.out.println("-1 -1 -1 -1");
                System.out.println(objs.size());
                int ctr = 0;
                for(DisplayObject d : objs){
                    System.out.println(ctr+" "+d.getX()+" "+d.getY()+" ");
                }
                break;
            }
            else{
                if(choice == 1){
                    System.out.println("Adding a WorldObject. size? (enter length, height, upper left x, and upper left y)");
                    int x = input.nextInt();
                    int y = input.nextInt();
                    int cx = input.nextInt();
                    int cy = input.nextInt();
                    DisplayObject temp = new DisplayObject(x,y,cx,cy);
                    objs.add(temp);
                    System.out.println("Is this good? (1 = yes, 0 = no)");
                    boolean isGood = (input.nextInt() == 1);
                    if(!isGood){
                        objs.remove(temp);
                        System.out.println("Got it, removed.");
                    }
                }
            }
        }
    }
    public void draw(){
        int ctr = 0;
        background(0,255,0);
        for(DisplayObject d : objs){
            fill(255,255,255);
            rectMode(CORNERS);
            rect(d.getX(), d.getY(), d.getX() + d.getLen(), d.getY() + d.getHeight());
            fill(0,0,0);
            textSize(20);
            text("WO "+ctr, d.getX(), d.getY(), d.getX() + d.getLen(), d.getY() + d.getHeight());
            ctr++;
        }
    }
    public void settings(){
        size(640,360);
    }
    public void setup(){
        frameRate(60);
    }

    public void mouseClicked() {
        System.out.println("You clicked: "+mouseX+" "+mouseY);
    }
}