package GameEngine;

import Game.SlightlyEvilerDead;
import physics.PhysicsObject;
import ray.rage.scene.SceneNode;

public class GameNPC {

    private SceneNode zombieNode;
    private SceneNode playerNode;
    private PhysicsObject zombiePhys;
    private SlightlyEvilerDead game;

    private float speed;
    private int damage;
    private int health;

    public GameNPC(SceneNode z, SceneNode p, PhysicsObject phys, SlightlyEvilerDead g, float s, int d, int h){
        zombieNode = z;
        playerNode = p;
        zombiePhys = phys;
        game = g;
        speed = s;
        damage = d;
        health =  h;
    }

    public void checkDistance(){
        if((Math.abs(playerNode.getLocalPosition().x() - zombieNode.getLocalPosition().x()) < 6) &&
           (Math.abs(playerNode.getLocalPosition().y() - zombieNode.getLocalPosition().y()) < 6) &&
           (Math.abs(playerNode.getLocalPosition().z() - zombieNode.getLocalPosition().z()) < 6)){
                zombieNode.lookAt(playerNode);
            zombiePhys.applyForce(zombieNode.getWorldForwardAxis().x() * speed, 0.0f, zombieNode.getWorldForwardAxis().z() * speed, 0.0f, 0.0f, 0.0f);
        }
    }

    public void hitPlayer(){

    }

    public void tookDamage(){

    }
}
