package Networking;

import ray.rage.scene.Entity;
import ray.rage.scene.SceneNode;
import ray.rml.Vector3;

import java.util.UUID;

public class GhostNPC {

    private UUID id;
    private SceneNode node;
    private Entity entity;
    private Vector3 position;
    private int health;
    private float speed;

    public GhostNPC(UUID id, Vector3 position) {
        this.id = id;
        this.position = position;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public SceneNode getNode() {
        return node;
    }

    public void setNode(SceneNode node) {
        this.node = node;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    private void setHealth(int h){
        this.health = h;
    }

    private void setSpeed(float s){
        this.speed = s;
    }
}
