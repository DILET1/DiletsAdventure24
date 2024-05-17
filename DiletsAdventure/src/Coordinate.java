public class Coordinate {
    double x;
    double y;
    public Coordinate(double x, double y){
        this.x = x;
        this.y = y;
    }
    public double getX(){
        return this.x;
    }
    public double getY(){
        return this.y;
    }
    public void addX(double a){
        this.x+=a;
    }
    public void addY(double a){
        this.y+=a;
    }

}
