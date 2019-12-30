package GameObjects;

import ray.rage.Engine;
import ray.rage.scene.Light;
import ray.rage.scene.SceneManager;
import ray.rage.scene.SceneNode;
import ray.rml.Vector3;

import java.awt.*;
import java.util.UUID;

public class GameLight {
    private Vector3 position;
    Light.Type type;
    Color specular;
    Color diffuse;
    String name;
    float attenuation;
    float range;
    private UUID id;

    public GameLight(Vector3 p, Light.Type lt, Color s, Color d, String n, float a, float r) {
        position = p;
        type = lt;
        specular = s;
        diffuse = d;
        id = UUID.randomUUID();
        name = n + "_" + id.toString();
        attenuation = a;
        range = r;
    }

    public SceneNode Create(SceneManager sm, SceneNode sn, Engine eng) {
        Light light = sm.createLight(name, type);
        light.setConstantAttenuation(attenuation);
        light.setDiffuse(diffuse);
        light.setSpecular(specular);
        light.setRange(range);
        SceneNode lightNode = sn.createChildSceneNode(name);
        lightNode.attachObject(light);
        return lightNode;
    }

    public String getName(){
        return name;
    }
}