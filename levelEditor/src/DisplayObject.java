public class DisplayObject {
    int length;
    int height;
    int x;
    int y;
    public DisplayObject(int len, int height, int x, int y){
        this.length = len;
        this.height = height;
        this.x = x;
        this.y = y;
    }
    int getLen(){
        return this.length;
    }
    int getHeight() {
        return this.height;
    }
    int getX(){
        return this.x;
    }
    int getY(){
        return this.y;
    }


}
