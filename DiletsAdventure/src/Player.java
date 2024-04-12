public class Player {
    private int x, y;
    public Player(){
        this.x = 0;
        this.y = 0;
    }
    void moveX(int amt){
        this.x+=amt;
    }
    void moveY(int amt){
        this.y+=amt;
    }
    int getX(){
        return this.x;
    }
    int getY(){
        return this.y;
    }

}
