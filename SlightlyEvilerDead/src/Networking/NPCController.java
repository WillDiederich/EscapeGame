package Networking;

import ray.rml.Vector3;
import ray.rml.Vector3f;

import java.util.ArrayList;

public class NPCController {

    private ArrayList<NPC> NPCList = new ArrayList<NPC>();

    public NPCController(){

    }

    // There will be 10-25 NPCs depending on stuff...
    public void setupNPCs(){
        NPCList.add(new NPC((Vector3) Vector3f.createFrom(0.0f, 2.0f, 0.0f), 100, 10.0f));
    }

    public void updateNPCs(){
        for(NPC npc : NPCList){

        }
    }

    public NPC getNPC(int x){
        return NPCList.get(x);
    }

    public int getNumOfNPCs(){
        return NPCList.size();
    }

    public Vector3 getPosition(int x){
        return NPCList.get(x).getPosition();
    }
}
