public class Coordinate {
    int x;
    int y;
    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public void addX(int a){
        this.x+=a;
    }
    public void addY(int a){
        this.y+=a;
    }

}
