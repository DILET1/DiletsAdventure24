import processing.core.PImage;

public class Item {
    private PImage sprite;
    private String name, description;
    public Item(String name, String description){
        this.name = name;
        this.description = description;
    }
    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public String toString(){
        return("Name: "+name +"\nDescription:"+description);
    }
}
