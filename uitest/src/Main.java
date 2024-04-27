import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class Main extends PApplet {
    PImage i1, i2, i3, i4, i5, i6;
    PFont uiFont;
    int resScalar = 2;
    void load(){
        i1 = loadImage("images/bottomBarBackground.png");
        i1.resize(200 * resScalar, 0);
        i2 = loadImage("images/eventLogBackground.png");
        i2.resize(200 * resScalar, 0);
        i3 = loadImage("images/questTabBackground.png");
        i3.resize(200 * resScalar, 0);
        i4 = loadImage("images/itembutton.png");
        i4.resize(60 * resScalar, 0);
        i5 = loadImage("images/pausebutton.png");
        i5.resize(60 * resScalar, 0);
        i6 = loadImage("images/questlogbutton.png");
        i6.resize(60 * resScalar, 0);
    }
    public static void main(String[] args) {
        System.out.println("Hello world!");
        PApplet.main("Main");
    }
    public void settings(){
        size(640 * resScalar,360* resScalar);
        load();
    }
    public void setup(){
        uiFont = createFont("CONSOLA.TTF", 10 * resScalar);

    }
    public void draw(){
        imageMode(CORNER);
        image(i1, 440* resScalar, 330* resScalar);
        image(i2, 440* resScalar, 120* resScalar);
        image(i3, 440* resScalar, 0* resScalar);
        image(i4, 445 * resScalar, 335 * resScalar);
        image(i5, 510 * resScalar, 335 * resScalar);
        image(i6, 575 * resScalar, 335 * resScalar);
        textFont(uiFont);
        text("wasg", 50, 200);
    }

}