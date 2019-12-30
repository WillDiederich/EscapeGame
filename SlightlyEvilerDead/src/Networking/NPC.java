package Networking;

import ray.rml.Vector3;

public class NPC {
    private Vector3 position;
    private int health;
    private float speed;

    public NPC(Vector3 pos, int h, float s){
        position = pos;
        health = h;
        speed = s;
    }

    public Vector3 getPosition(){
        return position;
    }

    public void updatePosition(Vector3 pos){
        this.position = pos;
    }
}
