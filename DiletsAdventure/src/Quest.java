import java.util.ArrayList;

public class Quest {
    ArrayList<String> steps = new ArrayList<>();
    ArrayList<Boolean> flags = new ArrayList<>();
    String name;
    int stepInd = 0;
    boolean done = false;
    public Quest(String name){
        this.name = name;
    }
    public void addStep(String desc){
        this.steps.add(desc);
        flags.add(false);
    }
    public String curStep(){
        return this.steps.get(stepInd);
    }
    public String getName(){
        return this.name;
    }
    public void progress(int a){
        this.flags.set(a, true);
        for(int i = 0; i < flags.size(); i++){
            if(!flags.get(i)){
                stepInd = i;
                return;
            }
        }
        stepInd = flags.size()-1;
        done = true;
        return;
    }
    public boolean questDone(){
        return done;
    }
    public void resetQuest(){
        flags.replaceAll(ignored -> false);
    }
    public void setName(String newName){
        name = newName;
    }
}
