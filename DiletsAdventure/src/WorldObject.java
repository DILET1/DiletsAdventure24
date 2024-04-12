public class WorldObject {
    private int lowx, lowy, hix, hiy; //lowx and lowy store x and y coords of the lower left corner, hix and hiy store the upper right x and y coords
    public WorldObject(int lowx, int lowy, int hix, int hiy){
        this.lowx = lowx;
        this.lowy = lowy;
        this.hix = hix;
        this.hiy = hiy;
    }
    public int getLowx(){
        return this.lowx;
    }
    public int getHix(){
        return this.hix;
    }
    public int getLowy(){
        return this.lowy;
    }
    public int getHiy(){
        return this.hiy;
    }
}
