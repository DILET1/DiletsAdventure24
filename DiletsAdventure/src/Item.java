import processing.core.PImage;

public class Item {
    private PImage sprite;
    private String name, description;
    public Item(String name, String description){
        this.name = name;
        this.description = description;
    }
    int questid = -1, step = -1;
    public Item(String name, String description, int questid, int step){
        this.name = name;
        this.description = description;
        this.questid = questid;
        this.step = step;
    }
    public int getQuestid(){
        return questid;
    }
    public int getStep(){
        return step;
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
