import java.util.ArrayList;

public class giveQuest extends Event{
    ArrayList<Quest> cq = new ArrayList<>();
    ArrayList<Quest> wq = new ArrayList<>();
    ArrayList<Quest> fq = new ArrayList<>();
    int id;
    String dialogue;
    public giveQuest(Player dil, boolean silent, String msg, int id, ArrayList<Quest> fq, ArrayList<Quest> cq, ArrayList<Quest> wq) {
        super(msg, silent, dil, -1,-1);
        this.id = id;
        this.cq = cq;
        this.wq = wq;
        this.dialogue = msg;
    }
    public String message(){
        if(!fq.contains(wq.get(id)) && !cq.contains(wq.get(id))){
            cq.add(wq.get(id));
            System.out.println("added questid "+id);
        }
        else{
            System.out.println("already have questid "+id);
        }
        return dialogue;
    }
}
