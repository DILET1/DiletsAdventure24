public class Projectile {
    int radius, damage, speed;
    double angle;
    public Projectile(int radius,int damage, int speed, double angle){
        this.radius = radius;
        this.damage = damage;
        this.speed = speed;
        this.angle = angle;
    }
    public double getDX(){
        return Math.cos(angle) * speed;
    }
    public double getDY(){
        return Math.sin(angle) * speed;
    }
    public int getDamage(){
        return this.damage;
    }
    public double getAngle(){return this.angle;}
}
