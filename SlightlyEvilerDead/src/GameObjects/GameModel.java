package GameObjects;

import java.io.IOException;
import java.util.UUID;

import ray.rage.Engine;
import ray.rage.asset.texture.TextureManager;
import ray.rage.rendersystem.Renderable.Primitive;
import ray.rage.scene.Entity;
import ray.rage.scene.SceneManager;
import ray.rage.scene.SceneNode;
import ray.rml.Vector3;

public class GameModel {
    private Vector3 position;
    private Vector3 minimum;
    private Vector3 maximum;
    private Vector3 size;

    private String texture;
    private String name;

    private UUID id;

    public GameModel(Vector3 p, Vector3 s, String n, String t){
        position = p;
        size = s;
        texture = t;
        id = UUID.randomUUID();
        name = n;
    }

    public SceneNode Create(SceneManager sm, SceneNode sn, Engine eng){
        try {
            Entity entity = sm.createEntity(name + "entity", texture);
            entity.setPrimitive(Primitive.TRIANGLES);

            SceneNode node = sn.createChildSceneNode(name + "node");
            node.attachObject(entity);
            node.scale(size);
            node.setLocalPosition(position);
            return node;
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public String getName(){
        return name;
    }

    public void setMinimum(Vector3 p){
        this.minimum = p;
    }

    public void setMaximum(Vector3 p){
        this.maximum = p;
    }

    public Vector3 getMinimum(){
        return this.minimum;
    }

    public Vector3 getMaximum(){
        return this.maximum;
    }
}